package com.xc.demo.chapter_4_5;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;

import java.util.ArrayList;

/**
 * @Description 游走一个有序路径
 * Created by xuec on 2017/5/31.
 */
public class TraversalPathDemo {

    public static void main(String[] args) {
        GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase("D://Neo4j CE 3.0.7//data//traversal-path.db");

        Transaction tx = graphDB.beginTx();

        try {
            Node A = graphDB.createNode();
            A.setProperty("name", "A");
            Node B = graphDB.createNode();
            B.setProperty("name", "B");
            Node C = graphDB.createNode();
            C.setProperty("name", "C");
            Node D = graphDB.createNode();
            D.setProperty("name", "D");

            A.createRelationshipTo(B, RelTypes.REL1);
            B.createRelationshipTo(C, RelTypes.REL2);
            C.createRelationshipTo(D, RelTypes.REL3);
            A.createRelationshipTo(C, RelTypes.REL2);

            TraversalDescription td = definePath();

            String result = traversalAndQuery(td, A);

            System.out.println(result);

            tx.success();
        } finally {
            tx.finish();
        }
    }

    /**
     * 定义路径中关系的顺序
     * @return 一个有序的路径定义
     */
    private static TraversalDescription definePath() {
        final ArrayList<RelationshipType> orderedPathContext = new ArrayList<>();
        orderedPathContext.add(RelTypes.REL1);
        orderedPathContext.add(RelTypes.REL2);
        orderedPathContext.add(RelTypes.REL3);

        TraversalDescription td = Traversal.description()
                .evaluator(new Evaluator() {
                    @Override
                    public Evaluation evaluate(Path path) {
                        if ( path.length() == 0 )
                        {
                            return Evaluation.EXCLUDE_AND_CONTINUE;
                        }
                        RelationshipType expectedType = orderedPathContext.get( path.length() - 1 );
                        boolean isExpectedType = path.lastRelationship()
                                .isType( expectedType );
                        boolean included = path.length() == orderedPathContext.size()
                                && isExpectedType;
                        boolean continued = path.length() < orderedPathContext.size()
                                && isExpectedType;
                        return Evaluation.of( included, continued );
                    }
                });

        return td;
    }

    /**
     * 执行一次遍历查询，并返回结果
     * @param td 有序路径的定义
     * @param node 路径起始的节点
     * @return 查询结果
     */
    private static String traversalAndQuery(TraversalDescription td, Node node) {
        StringBuilder output = new StringBuilder("Path: \n");

        Traverser traverser = td.traverse(node);
        PathPrinter pathPrinter = new PathPrinter("name");
        for(Path path : traverser) {
            output.append(Traversal.pathToString(path, pathPrinter));
        }

        return output.toString();
    }

    /**
     * 自定义一个类，用于格式化输出
     */
    static class PathPrinter implements Traversal.PathDescriptor<Path> {

        private final String nodePropertyKey;

        public PathPrinter(String nodePropertyKey) {
            this.nodePropertyKey = nodePropertyKey;
        }


        @Override
        public String nodeRepresentation(Path propertyContainers, Node node) {
            return "(" + node.getProperty(nodePropertyKey, "") + ")";
        }

        @Override
        public String relationshipRepresentation(Path propertyContainers, Node from, Relationship relationship) {
            String prefix = "--", suffix = "--";
            if (from.equals(relationship.getEndNode())) {
                prefix = "<--";
            } else {
                suffix = "-->";
            }
            return prefix + "[" + relationship.getType().name() + "]" + suffix;
        }
    }


    private enum RelTypes implements RelationshipType {
        REL1,REL2,REL3;
    }
}
