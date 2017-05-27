package com.xc.demos;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;

import java.io.File;

/**
 * @Description 创建带索引的用户数据库
 * Created by xuec on 2017/5/27.
 */
public class Neo4jIndexDemo {

    private static File file;
    private static GraphDatabaseService graphDB;
    private static final String USERNAME_KEY = "name";
    private static Index<Node> nodeIndex;

    static {
        file = new File("D://Neo4j CE 3.0.7//data//user.db");
        graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(file);

    }

    public static void main(String[] args) {
        try (Transaction tx = graphDB.beginTx()){
            nodeIndex = graphDB.index().forNodes("nodes");
            Node useReferenceNode = graphDB.createNode();
            //创建用户并添加索引
            for(int id = 0; id < 100; id ++) {
                Node userNode = createAndIndexUser(idToUserName(id));
                useReferenceNode.createRelationshipTo(userNode, RelTypes.USER);
            }

            //通过ID查找用户
            int idToFind = 45;
            Node foundUser = nodeIndex.get(USERNAME_KEY, idToUserName(45)).getSingle();
            System.out.println("The username of user" + idToFind + " is " + foundUser.getProperty(USERNAME_KEY));

            tx.success();
        }

        graphDB.shutdown();
    }

    /**
     * 生成用户名
     * @param id
     * @return
     */
    private static String idToUserName(final int id) {
        return "user" + id + "@neo4j.org";
    }

    /**
     * 创建用户，并以名字为索引
     * @param username
     * @return
     */
    private static Node createAndIndexUser(final String username) {
        Node node = graphDB.createNode();
        node.setProperty(USERNAME_KEY, username);
        nodeIndex.add(node, USERNAME_KEY, username);
        return  node;
    }

    private static enum RelTypes implements RelationshipType {
        USER_REFERENCE, USER
    }
}
