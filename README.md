# auto-coin-trader

### ��������
 - ������ ���� ����
 - https://post.naver.com/viewer/postView.nhn?volumeNo=15975365&memberNo=40921089
 - ���߼� �����߰�? (https://jsp-dev.tistory.com/178?category=824999)
 
 
### �����
  - upbit open api key ���� 
  - ������ mariadb ��ġ , ���̺� ����
  - application.properties �� upbit key , mariadb �ּ� ���� ����
  - build �ؼ� jar ���� �������� ����
  
### mariadb ���̺� ��Ű��
```
CREATE TABLE coin.`vloatility_range_breakout` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date_string` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `datetime_id` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `coin_type` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `opening_price` decimal(20,8) DEFAULT NULL,
  `target_price` decimal(20,8) DEFAULT NULL,
  `target_count` decimal(20,8) DEFAULT NULL,
  `buy_uuid` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `buy_price` decimal(20,8) DEFAULT NULL,
  `buy_count` decimal(20,8) DEFAULT NULL,
  `sell_uuid` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `sell_price` decimal(20,8) DEFAULT NULL,
  `sell_count` decimal(20,8) DEFAULT NULL,
  `fee` decimal(20,8) DEFAULT NULL,
  `noise_rate` decimal(20,8) DEFAULT NULL,
  `moving_avg_score` decimal(20,8) DEFAULT NULL,
  `vloatility_invest_rate` decimal(20,8) DEFAULT NULL,
  `k` decimal(20,8) DEFAULT NULL,
  `status` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `buy_at` datetime DEFAULT NULL,
  `sell_at` datetime DEFAULT NULL,
PRIMARY KEY (`id`),
KEY `idx1` (`coin_type`,`datetime_id`,`date_string`),
KEY `idx2` (`coin_type`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin'
```

### ����
```
nohup java -Xms256m -Xmx256m -jar auto-coin-trader-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 & 
```

