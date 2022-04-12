package com.myspring.spring.qna;

import org.apache.ibatis.jdbc.SQL;

public class QnaUtils {
//  select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') AND type in ('general', 'product', 'productNotice', 'productReply', 'generalReply') order by originalNo desc, qnaNo asc limit ${start}, ${perPage}")
//	@Select("select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') AND type in ('cancel', 'change', 'changeaddress', 'cancelNotice', 'cancelReply', 'changeReply', 'changeaddressReply') order by originalNo desc, qnaNo asc limit ${start}, ${perPage}") 
//	"select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') AND type in('delivery', 'deliveryNotice', 'deliveryReply') order by originalNo desc, qnaNo asc limit #{start}, #{perPage}"
//	@Select("select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') AND type in ('return', 'exchange', 'error', 'returnNotice', 'returnReply', 'exchangeReply', 'errorReply') order by originalNo desc, qnaNo asc limit #{start}, #{perPage}")

	public String getQnaListByType(int start, int perPage, String search, String searchWord, String type) {
		SQL sql = new SQL() {
			{
				SELECT("*");
				FROM("qnatable q");
				LEFT_OUTER_JOIN("producttable p on p.productNo = q.productNo");
				switch (type) {
				case "product":
					WHERE("q.type in ('general', 'product', 'productNotice', 'productReply', 'generalReply')");
					break;
				case "delivery":
					WHERE("q.type in('delivery', 'deliveryNotice', 'deliveryReply')");
					break;
				case "beforeDelivery":
					WHERE("q.type in ('cancel', 'change', 'changeAddress', 'cancelNotice', 'cancelReply', 'changeReply', 'changeaddressReply')");
					break;
				case "afterDelivery":
					WHERE("q.type in ('return', 'exchange', 'error', 'returnNotice', 'returnReply', 'exchangeReply', 'errorReply')");
					break;
				}
				if(search.equals("reply")) {
					AND();
					WHERE("q.reply = false and q.qnaNo = q.originalNo");
				}
				if (searchWord != null && !searchWord.equals("")) {
					AND();
					String[] words = searchWord.split(",");
					String temp = null;
					for (int i = 0; i < words.length; i++) {
						if (i == 0) {
							temp = words[i];
						} else {
							temp = temp + "|" + words[i];
						}
					}
					switch (search) {
					case "id":
						WHERE("REGEXP_LIKE(q.id, '" + temp + "')");
						break;
					case "productName":
						WHERE("REGEXP_LIKE(p.productName, '" + temp + "')");
						break;
					}
				}
				ORDER_BY("originalNo desc, qnaNo asc");
				LIMIT(perPage);
				OFFSET(start);
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}

	public String getQnaCountByType(String search, String searchWord, String type) {
		SQL sql = new SQL() {
			{
				SELECT("count(*)");
				FROM("qnatable q");
				LEFT_OUTER_JOIN("producttable p on p.productNo = q.productNo");
				switch (type) {
				case "product":
					WHERE("q.type in ('general', 'product', 'productNotice', 'productReply', 'generalReply')");
					break;
				case "delivery":
					WHERE("q.type in('delivery', 'deliveryNotice', 'deliveryReply')");
					break;
				case "beforeDelivery":
					WHERE("q.type in ('cancel', 'change', 'changeAddress', 'cancelNotice', 'cancelReply', 'changeReply', 'changeaddressReply')");
					break;
				case "afterDelivery":
					WHERE("q.type in ('return', 'exchange', 'error', 'returnNotice', 'returnReply', 'exchangeReply', 'errorReply')");
					break;
				}
				if(search.equals("reply")) {
					AND();
					WHERE("q.reply = false and q.qnaNo = q.originalNo");
				}
				if (searchWord != null && !searchWord.equals("")) {
					AND();
					String[] words = searchWord.split(",");
					String temp = null;
					for (int i = 0; i < words.length; i++) {
						if (i == 0) {
							temp = words[i];
						} else {
							temp = temp + "|" + words[i];
						}
					}
					switch (search) {
					case "id":
						WHERE("REGEXP_LIKE(q.id, '" + temp + "')");
						break;
					case "productName":
						WHERE("REGEXP_LIKE(p.productName, '" + temp + "')");
						break;
					}
				}
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}

	public String getQnaList(int start, int perPage, String search, String searchWord, int productNo, String id) {
		SQL sql = new SQL() {
			{
				SELECT("*");
				FROM("qnatable q");
				LEFT_OUTER_JOIN("producttable p on p.productNo = q.productNo");
				if (searchWord != null && !searchWord.equals("")) {
					AND();
					String[] words = searchWord.split(",");
					String temp = null;
					for (int i = 0; i < words.length; i++) {
						if (i == 0) {
							temp = words[i];
						} else {
							temp = temp + "|" + words[i];
						}
					}
					switch (search) {
					case "id":
						WHERE("REGEXP_LIKE(q.id, '" + temp + "')");
						break;
					case "productName":
						WHERE("REGEXP_LIKE(p.productName, '" + temp + "')");
						break;
					}
				}
				if (productNo != 0) {
					AND();
					WHERE("q.productNo = " + productNo);
				}
				if (id != null) {
					AND();
					WHERE("q.id = '" + id + "'");
				}
				ORDER_BY("originalNo desc, qnaNo asc");
				LIMIT(perPage);
				OFFSET(start);
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}

	public String getQnaCount(String search, String searchWord, int productNo, String id) {
		SQL sql = new SQL() {
			{
				SELECT("count(*)");
				FROM("qnatable q");
				LEFT_OUTER_JOIN("producttable p on p.productNo = q.productNo");
				if (searchWord != null && !searchWord.equals("")) {
					AND();
					String[] words = searchWord.split(",");
					String temp = null;
					for (int i = 0; i < words.length; i++) {
						if (i == 0) {
							temp = words[i];
						} else {
							temp = temp + "|" + words[i];
						}
					}
					switch (search) {
					case "id":
						WHERE("REGEXP_LIKE(q.id, '" + temp + "')");
						break;
					case "productName":
						WHERE("REGEXP_LIKE(p.productName, '" + temp + "')");
						break;
					}
				}
				if (productNo != 0) {
					AND();
					WHERE("q.productNo = " + productNo);
				}
				if (id != null) {
					AND();
					WHERE("q.id = '" + id + "'");
				}
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}

}