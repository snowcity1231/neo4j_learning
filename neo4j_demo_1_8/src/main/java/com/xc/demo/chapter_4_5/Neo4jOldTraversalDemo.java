package com.xc.demo.chapter_4_5;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * @Description 老的遍历查询API
 * Created by xuec on 2017/5/31.
 */
public class Neo4jOldTraversalDemo {

    public static void main(String[] args) {
        //TODO 这里还是采用了老版本<1.7>的遍历方法，所以客户端也无法打开该数据库，前端无法展示
        GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase("D://Neo4j CE 3.0.7//data//traversal-Matrix-old.db");

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


            String result = printFriends(person);

            System.out.println(result);

            result = printHackers(person);
            System.out.println(result);
        } finally {
            tx.finish();
        }
    }

    private static Traverser getFriends(final Node person) {
        return person.traverse(org.neo4j.graphdb.Traverser.Order.BREADTH_FIRST, StopEvaluator.END_OF_GRAPH, ReturnableEvaluator.ALL_BUT_START_NODE, RelTypes.KNOWS, Direction.OUTGOING);
    }

    private static String printFriends(Node neoNode) {
        int numberOfFriends = 0;
        StringBuilder output = new StringBuilder();
        output.append(neoNode.getProperty("name")).append("'s friends:\n");
        Traverser friendsTraverser = getFriends(neoNode);
        for(Node friendNode : friendsTraverser) {
            output.append("At depth ");
            output.append(friendsTraverser.currentPosition().depth());
            output.append(" => ");
            output.append(friendNode.getProperty("name"));
            output.append("\n");
            numberOfFriends ++;
        }
        output.append("Number of Friends found: ").append(numberOfFriends).append("\n");

        return output.toString();
    }

    private static Traverser findHackers(final Node startNode) {
        return startNode.traverse(Traverser.Order.BREADTH_FIRST, StopEvaluator.END_OF_GRAPH, new ReturnableEvaluator() {
            @Override
            public boolean isReturnableNode(TraversalPosition currentPos) {
                return !currentPos.isStartNode() && currentPos.lastRelationshipTraversed().isType(RelTypes.CODED_BY);
            }
        }, RelTypes.CODED_BY, Direction.OUTGOING, RelTypes.KNOWS, Direction.OUTGOING);
    }

    private static String printHackers(Node neoNode) {
        StringBuilder output = new StringBuilder("Hackers:\n");
        int numberOfHackers = 0;
        Traverser traverser = findHackers(neoNode);
        for(Node hackerNode : traverser) {
            output.append("At depth ").append(traverser.currentPosition().depth()).append(" => ");
            output.append(hackerNode.getProperty("name")).append("\n");
            numberOfHackers ++;
        }
        output.append("Number of hackers found:").append(numberOfHackers).append("\n");

        return output.toString();
    }

    private enum  RelTypes implements RelationshipType {
        KNOWS, CODED_BY;
    }


}
