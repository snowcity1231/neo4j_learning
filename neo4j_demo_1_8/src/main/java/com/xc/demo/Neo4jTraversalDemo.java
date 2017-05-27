package com.xc.demo;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;

import java.util.ArrayList;

/**
 * @Description neo4j实现遍历查询
 *
 *
 * Created by xuec on 2017/5/27.
 */
public class Neo4jTraversalDemo {

    public static void main(String[] args) {
        //TODO 这里还是采用了老版本<1.7>的遍历方法，新版本的教程还在寻找中，所以客户端也无法打开该数据库，前端无法展示
        GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase("D://Neo4j CE 3.0.7//data//traversal-Matrix.db");

        Transaction tx = graphDB.beginTx();
        try {
            //创建数据
            Node person = graphDB.createNode();
            person.setProperty("name", "Thomas Anderson");
            Node person1 = graphDB.createNode();
            person1.setProperty("name", "Trinity");
            Node person2 = graphDB.createNode();
            person2.setProperty("name", "Morpheus");
            Node person3 = graphDB.createNode();
            person3.setProperty("name", "Cypher");
            Node person4 = graphDB.createNode();
            person4.setProperty("name", "Agent Smith");
            person.createRelationshipTo(person1, RelTypes.KNOWS);
            person.createRelationshipTo(person2, RelTypes.KNOWS);
            person1.createRelationshipTo( person2, RelTypes.KNOWS );
            person2.createRelationshipTo( person3, RelTypes.KNOWS );
            person3.createRelationshipTo( person4, RelTypes.KNOWS );
            person1.createRelationshipTo( person3, RelTypes.KNOWS );

            Node hacker = graphDB.createNode();
            hacker.setProperty("name", "The Architect");
            person4.createRelationshipTo(hacker, RelTypes.CODED_BY);

            printFriends(person);

            printHackers(person);
        } finally {
            tx.finish();
        }
    }

    /**
     * 遍历获取朋友以及朋友的朋友
     * @param person 人物节点
     * @return 遍历该任务认识的人
     */
    private static Traverser getFriends(final Node person) {
        TraversalDescription td = Traversal.description()
                .breadthFirst()
                .relationships(RelTypes.KNOWS, Direction.OUTGOING)
                .evaluator(Evaluators.excludeStartPosition());
        return td.traverse(person);
    }

    /**
     * 打印输入结果
     * @param neoNode 人物节点
     */
    private static void printFriends(Node neoNode) {
        int numberOfFriends = 0;
        StringBuilder output = new StringBuilder(neoNode.getProperty("name") + "'s friends:\n");
        Traverser freindsTraversal = getFriends(neoNode);
        for(Path friendPath : freindsTraversal) {
            output.append("At depth ");
            output.append(friendPath.length()).append(" => ").append(friendPath.endNode().getProperty("name")).append("\n");
            numberOfFriends ++;
        }
        output.append("Number of friends found : ").append(numberOfFriends).append("\n");

        System.out.println(output.toString());
    }

    /**
     * 寻找谁编写了黑客帝国
     * @param startNode 人物节点
     * @return 遍历出编写的人
     */
    private static Traverser findHackers(final Node startNode) {
        TraversalDescription td = Traversal.description()
                .breadthFirst()
                .relationships(RelTypes.KNOWS, Direction.OUTGOING)
                .relationships(RelTypes.CODED_BY, Direction.OUTGOING)
                .evaluator(Evaluators.includeWhereLastRelationshipTypeIs(RelTypes.CODED_BY));
        return td.traverse(startNode);
    }

    private static void printHackers(final Node startNode) {
        StringBuilder output = new StringBuilder("Hackers:\n");
        int numberOfHackers = 0;
        Traverser traverser = findHackers(startNode);
        for (Path path : traverser) {
            output.append("At depth ").append(path.length()).append(" => ");
            output.append(path.endNode().getProperty("name")).append("\n");
            numberOfHackers ++;
        }
        output.append("Number of hackers found : ").append(numberOfHackers).append("\n");

        System.out.println(output.toString());
    }

    /**
     * 定义如何游走这个路径
     */
    private static void definePath() {
        final ArrayList<RelationshipType> orderedPathContext = new ArrayList<>();
//        orderedPathContext.add(RelTypes.REL1);
//        orderedPathContext.add(RelTypes.REL2);
//        orderedPathContext.add(RelTypes.REL3);

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
    }

    private enum  RelTypes implements RelationshipType {
        KNOWS, CODED_BY;
    }
}
