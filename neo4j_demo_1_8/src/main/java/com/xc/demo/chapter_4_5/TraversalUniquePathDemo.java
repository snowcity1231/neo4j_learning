package com.xc.demo.chapter_4_5;

import org.neo4j.cypher.internal.mutation.Traverse;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;

/**
 * @Description 在遍历查询中的唯一路径
 * Created by xuec on 2017/5/31.
 */
public class TraversalUniquePathDemo {

    public static void main(String[] args) {
        GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase("D://Neo4j CE 3.0.7//data//traversal-path-unique.db");

        Transaction tx = graphDB.beginTx();

        try {
            Node pet1 = graphDB.createNode();
            pet1.setProperty("name", "Pet1");
            Node pet2 = graphDB.createNode();
            pet2.setProperty("name", "Pet2");
            Node pet0 = graphDB.createNode();
            pet0.setProperty("name", "Pet0");
            Node pet3 = graphDB.createNode();
            pet3.setProperty("name", "Pet3");
            Node person1 = graphDB.createNode();
            person1.setProperty("name", "Principal1");
            Node person2 = graphDB.createNode();
            person2.setProperty("name", "Principal2");

            person1.createRelationshipTo(pet1, RelTypes.OWNS);
            person1.createRelationshipTo(pet3, RelTypes.OWNS);
            person2.createRelationshipTo(pet2, RelTypes.OWNS);
            pet0.createRelationshipTo(pet1, RelTypes.DESCENDANT);
            pet0.createRelationshipTo(pet2, RelTypes.DESCENDANT);
            pet0.createRelationshipTo(pet3, RelTypes.DESCENDANT);

            //查询从pet0到person1的路径
            Traverser traverser = definePath(pet0, person1);
            String result = queryResult(traverser);
            //在 path.toString() 的默认实现中，(1)–[knows,2]–>(4) 表示一个ID=1的节点通过一个ID=2，关系类型为 knows 的关系连接到了一个ID=4的节点上。
            System.out.println(result);

        } finally {
            tx.finish();
        }
    }

    /**
     * 获取一条遍历路径
     * @param start 起始节点
     * @param target 目标节点
     * @return 遍历路径结果
     */
    private static Traverser definePath(final Node start, final Node target) {
        System.out.println("start:" + start.getId() + "-" + start.getProperty("name"));
        System.out.println("target:" + target.getId() + "-" + target.getProperty("name"));
        //遍历查询的 Uniqueness设置为NODE_PATH，来保证节点可以被遍历不止一次，这样可以使一些有不同节点但有一部分相同路径的结果也可以被返回
        TraversalDescription td = Traversal.description().uniqueness(Uniqueness.NODE_PATH)
                .evaluator(new Evaluator() {
                    @Override
                    public Evaluation evaluate(Path path) {
                        if (path.endNode().equals(target)) {
                            return Evaluation.INCLUDE_AND_PRUNE;
                        }
                        return Evaluation.EXCLUDE_AND_CONTINUE;
                    }
                });
        Traverser results = td.traverse(start);

        //这里可以创建一个新的TravesalDescription，设置Uniqueness为NODE_GLOBAL,这时将只有一条路径被返回
//        TraversalDescription nodeGlobalTd = td.uniqueness(Uniqueness.NODE_GLOBAL);
//        Traverser results = nodeGlobalTd.traverse(start);
        return results;
    }

    private static String queryResult(Traverser result) {
        StringBuilder output = new StringBuilder("Path:\n");

        for(Path path : result) {
            output.append(Traversal.defaultPathToString(path));
            output.append("\n");
        }

        return output.toString();
    }

    private enum RelTypes implements RelationshipType {
        OWNS,DESCENDANT;
    }
}
