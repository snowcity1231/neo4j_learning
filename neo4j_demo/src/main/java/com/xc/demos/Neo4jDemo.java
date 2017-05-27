package com.xc.demos;

import java.io.File;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.xc.demos.label.MyLabels;
import com.xc.demos.label.MyRelationshipTypes;

/** 
* @ClassName: Neo4jDemo 
* @Description: TODO
* @author xuechen
* @date 2017年1月22日 下午3:50:11
*  
*/
public class Neo4jDemo {

	public static void main(String[] args) {
		
		//指定Neo4j存储路径
		File file = new File("D://Neo4j CE 3.0.7//data//graph.db");
		GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(file);
		
		try(Transaction tx = graphDB.beginTx()) {
			/**
			 * 新增User节点
			 * 每个节点设置name属性
			 * 添加Label以区分节点类型
			 */
			Node user1 = graphDB.createNode();
			user1.setProperty("name", "John Johnson");
			user1.addLabel(MyLabels.USERS);
			
			Node user2 = graphDB.createNode();
			user2.setProperty("name", "Kate Smith");
	        user2.addLabel(MyLabels.USERS);
	        
	        Node user3 = graphDB.createNode();
	        user3.setProperty("name", "Jack Jeffries");
	        user3.addLabel(MyLabels.USERS);
	        
	        /**
	         * 为user1添加Friend关系
	         * 注：Neo4j的关系是有向的箭头，一版Friends的关系是双向的
	         * 	   此处简单期间，关系设置为单向的，不会影响后面的查询
	         */
	        user1.createRelationshipTo(user2, MyRelationshipTypes.IS_FRIEND_OF);
	        user1.createRelationshipTo(user3, MyRelationshipTypes.IS_FRIEND_OF);
	        
	        /**
	         * 新建Movie节点
	         */
	        Node movie1 = graphDB.createNode();
	        movie1.setProperty("name", "Fargo");
	        movie1.addLabel(MyLabels.MOVIES);
	        
	        Node movie2 = graphDB.createNode();
	        movie2.setProperty("name", "Alien");
	        movie2.addLabel(MyLabels.MOVIES);
	        
	        Node movie3 = graphDB.createNode();
	        movie3.setProperty("name", "Heat");
	        movie3.addLabel(MyLabels.MOVIES);
	        
	        /**
	         * 为User节点与Movie节点添加HAS_SEEN关系
	         * HAS_SEEN关系设置stars属性
	         */
	        Relationship relationship1 = user1.createRelationshipTo(movie1, MyRelationshipTypes.HAS_SEEN);
	        relationship1.setProperty("stars", 5);
	        
	        Relationship relationship2 = user2.createRelationshipTo(movie3, MyRelationshipTypes.HAS_SEEN);
	        relationship2.setProperty("stars", 3);
	        Relationship relationship6 = user2.createRelationshipTo(movie2, MyRelationshipTypes.HAS_SEEN);
	        relationship6.setProperty("star3", 6);
	        
	        Relationship relationship3 = user3.createRelationshipTo(movie1, MyRelationshipTypes.HAS_SEEN);
	        relationship3.setProperty("stars", 4);
	        Relationship relationship4 = user3.createRelationshipTo(movie2, MyRelationshipTypes.HAS_SEEN);
	        relationship4.setProperty("stars", 5);

	        //删除电影节点Heat
			movie3.getSingleRelationship(MyRelationshipTypes.HAS_SEEN, Direction.INCOMING).delete();
			movie3.delete();
	        
	        
	        System.out.println("成功");
	        
	        tx.success();
		}
		
		//关闭数据库
		graphDB.shutdown();
	}
}
