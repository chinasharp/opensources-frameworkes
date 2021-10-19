CREATE TABLE demo_user  (
  id bigint(20) NOT NULL COMMENT '主键id',
  account varchar(32) NULL DEFAULT NULL COMMENT '账号',
  name varchar(32) NULL DEFAULT NULL COMMENT '姓名',
  password varchar(32) NULL DEFAULT NULL COMMENT '密码',
  phone varchar(32) NULL DEFAULT NULL COMMENT '手机号',
  member_card_no varchar(32) NULL DEFAULT NULL COMMENT '会员卡号',
  address varchar(128) NULL DEFAULT NULL COMMENT '地址',
  application_id int(11) NULL DEFAULT NULL COMMENT '应用id',
  create_person varchar(32) NULL DEFAULT NULL COMMENT '创建人',
  create_person_id bigint(20) NOT NULL COMMENT '创建人id',
  create_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '创建时间',
  create_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '创建时间戳',
  update_person varchar(32) NULL DEFAULT NULL COMMENT '更新人',
  update_person_id bigint(20) NOT NULL COMMENT '更新人id',
  update_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '更新时间',
  update_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '更新时间戳',
  dr varchar(255) NULL DEFAULT NULL COMMENT '逻辑删除 0正常  1 逻辑删除',
  PRIMARY KEY (id) USING BTREE,
  UNIQUE INDEX idx_user_account(account) USING BTREE,
  INDEX idx_user_phone(phone) USING BTREE,
  INDEX idx_user_member_card_no(member_card_no) USING BTREE
) ENGINE = InnoDB;





CREATE TABLE demo_sharding_user_0  (
  id bigint(20) NOT NULL COMMENT '主键id',
  account varchar(32) NULL DEFAULT NULL COMMENT '账号',
  name varchar(32) NULL DEFAULT NULL COMMENT '姓名',
  password varchar(32) NULL DEFAULT NULL COMMENT '密码',
  phone varchar(32) NULL DEFAULT NULL COMMENT '手机号',
  member_card_no varchar(32) NULL DEFAULT NULL COMMENT '会员卡号',
  address varchar(128) NULL DEFAULT NULL COMMENT '地址',
  application_id int(11) NULL DEFAULT NULL COMMENT '应用id',
  create_person varchar(32) NULL DEFAULT NULL COMMENT '创建人',
  create_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '创建时间',
  create_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '创建时间戳',
  update_person varchar(32) NULL DEFAULT NULL COMMENT '更新人',
  update_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '更新时间',
  update_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '更新时间戳',
  dr varchar(255) NULL DEFAULT NULL COMMENT '逻辑删除 0正常  1 逻辑删除',
  PRIMARY KEY (id) USING BTREE,
  UNIQUE INDEX idx_user_account(account) USING BTREE,
  INDEX idx_user_phone(phone) USING BTREE,
  INDEX idx_user_member_card_no(member_card_no) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


CREATE TABLE demo_sharding_user_1  (
  id bigint(20) NOT NULL COMMENT '主键id',
  account varchar(32) NULL DEFAULT NULL COMMENT '账号',
  name varchar(32) NULL DEFAULT NULL COMMENT '姓名',
  password varchar(32) NULL DEFAULT NULL COMMENT '密码',
  phone varchar(32) NULL DEFAULT NULL COMMENT '手机号',
  member_card_no varchar(32) NULL DEFAULT NULL COMMENT '会员卡号',
  address varchar(128) NULL DEFAULT NULL COMMENT '地址',
  application_id int(11) NULL DEFAULT NULL COMMENT '应用id',
  create_person varchar(32) NULL DEFAULT NULL COMMENT '创建人',
  create_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '创建时间',
  create_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '创建时间戳',
  update_person varchar(32) NULL DEFAULT NULL COMMENT '更新人',
  update_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '更新时间',
  update_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '更新时间戳',
  dr varchar(255) NULL DEFAULT NULL COMMENT '逻辑删除 0正常  1 逻辑删除',
  PRIMARY KEY (id) USING BTREE,
  UNIQUE INDEX idx_user_account(account) USING BTREE,
  INDEX idx_user_phone(phone) USING BTREE,
  INDEX idx_user_member_card_no(member_card_no) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


CREATE TABLE demo_sharding_user_2  (
  id bigint(20) NOT NULL COMMENT '主键id',
  account varchar(32) NULL DEFAULT NULL COMMENT '账号',
  name varchar(32) NULL DEFAULT NULL COMMENT '姓名',
  password varchar(32) NULL DEFAULT NULL COMMENT '密码',
  phone varchar(32) NULL DEFAULT NULL COMMENT '手机号',
  member_card_no varchar(32) NULL DEFAULT NULL COMMENT '会员卡号',
  address varchar(128) NULL DEFAULT NULL COMMENT '地址',
  application_id int(11) NULL DEFAULT NULL COMMENT '应用id',
  create_person varchar(32) NULL DEFAULT NULL COMMENT '创建人',
  create_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '创建时间',
  create_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '创建时间戳',
  update_person varchar(32) NULL DEFAULT NULL COMMENT '更新人',
  update_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '更新时间',
  update_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '更新时间戳',
  dr varchar(255) NULL DEFAULT NULL COMMENT '逻辑删除 0正常  1 逻辑删除',
  PRIMARY KEY (id) USING BTREE,
  UNIQUE INDEX idx_user_account(account) USING BTREE,
  INDEX idx_user_phone(phone) USING BTREE,
  INDEX idx_user_member_card_no(member_card_no) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


CREATE TABLE demo_sharding_user_3  (
  id bigint(20) NOT NULL COMMENT '主键id',
  account varchar(32) NULL DEFAULT NULL COMMENT '账号',
  name varchar(32) NULL DEFAULT NULL COMMENT '姓名',
  password varchar(32) NULL DEFAULT NULL COMMENT '密码',
  phone varchar(32) NULL DEFAULT NULL COMMENT '手机号',
  member_card_no varchar(32) NULL DEFAULT NULL COMMENT '会员卡号',
  address varchar(128) NULL DEFAULT NULL COMMENT '地址',
  application_id int(11) NULL DEFAULT NULL COMMENT '应用id',
  create_person varchar(32) NULL DEFAULT NULL COMMENT '创建人',
  create_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '创建时间',
  create_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '创建时间戳',
  update_person varchar(32) NULL DEFAULT NULL COMMENT '更新人',
  update_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '更新时间',
  update_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '更新时间戳',
  dr varchar(255) NULL DEFAULT NULL COMMENT '逻辑删除 0正常  1 逻辑删除',
  PRIMARY KEY (id) USING BTREE,
  UNIQUE INDEX idx_user_account(account) USING BTREE,
  INDEX idx_user_phone(phone) USING BTREE,
  INDEX idx_user_member_card_no(member_card_no) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE demo_sharding_user_4  (
  id bigint(20) NOT NULL COMMENT '主键id',
  account varchar(32) NULL DEFAULT NULL COMMENT '账号',
  name varchar(32) NULL DEFAULT NULL COMMENT '姓名',
  password varchar(32) NULL DEFAULT NULL COMMENT '密码',
  phone varchar(32) NULL DEFAULT NULL COMMENT '手机号',
  member_card_no varchar(32) NULL DEFAULT NULL COMMENT '会员卡号',
  address varchar(128) NULL DEFAULT NULL COMMENT '地址',
  application_id int(11) NULL DEFAULT NULL COMMENT '应用id',
  create_person varchar(32) NULL DEFAULT NULL COMMENT '创建人',
  create_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '创建时间',
  create_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '创建时间戳',
  update_person varchar(32) NULL DEFAULT NULL COMMENT '更新人',
  update_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '更新时间',
  update_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '更新时间戳',
  dr varchar(255) NULL DEFAULT NULL COMMENT '逻辑删除 0正常  1 逻辑删除',
  PRIMARY KEY (id) USING BTREE,
  UNIQUE INDEX idx_user_account(account) USING BTREE,
  INDEX idx_user_phone(phone) USING BTREE,
  INDEX idx_user_member_card_no(member_card_no) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE demo_sharding_user_5  (
  id bigint(20) NOT NULL COMMENT '主键id',
  account varchar(32) NULL DEFAULT NULL COMMENT '账号',
  name varchar(32) NULL DEFAULT NULL COMMENT '姓名',
  password varchar(32) NULL DEFAULT NULL COMMENT '密码',
  phone varchar(32) NULL DEFAULT NULL COMMENT '手机号',
  member_card_no varchar(32) NULL DEFAULT NULL COMMENT '会员卡号',
  address varchar(128) NULL DEFAULT NULL COMMENT '地址',
  application_id int(11) NULL DEFAULT NULL COMMENT '应用id',
  create_person varchar(32) NULL DEFAULT NULL COMMENT '创建人',
  create_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '创建时间',
  create_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '创建时间戳',
  update_person varchar(32) NULL DEFAULT NULL COMMENT '更新人',
  update_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '更新时间',
  update_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '更新时间戳',
  dr varchar(255) NULL DEFAULT NULL COMMENT '逻辑删除 0正常  1 逻辑删除',
  PRIMARY KEY (id) USING BTREE,
  UNIQUE INDEX idx_user_account(account) USING BTREE,
  INDEX idx_user_phone(phone) USING BTREE,
  INDEX idx_user_member_card_no(member_card_no) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE demo_sharding_user_6  (
  id bigint(20) NOT NULL COMMENT '主键id',
  account varchar(32) NULL DEFAULT NULL COMMENT '账号',
  name varchar(32) NULL DEFAULT NULL COMMENT '姓名',
  password varchar(32) NULL DEFAULT NULL COMMENT '密码',
  phone varchar(32) NULL DEFAULT NULL COMMENT '手机号',
  member_card_no varchar(32) NULL DEFAULT NULL COMMENT '会员卡号',
  address varchar(128) NULL DEFAULT NULL COMMENT '地址',
  application_id int(11) NULL DEFAULT NULL COMMENT '应用id',
  create_person varchar(32) NULL DEFAULT NULL COMMENT '创建人',
  create_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '创建时间',
  create_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '创建时间戳',
  update_person varchar(32) NULL DEFAULT NULL COMMENT '更新人',
  update_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '更新时间',
  update_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '更新时间戳',
  dr varchar(255) NULL DEFAULT NULL COMMENT '逻辑删除 0正常  1 逻辑删除',
  PRIMARY KEY (id) USING BTREE,
  UNIQUE INDEX idx_user_account(account) USING BTREE,
  INDEX idx_user_phone(phone) USING BTREE,
  INDEX idx_user_member_card_no(member_card_no) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE demo_sharding_user_7  (
  id bigint(20) NOT NULL COMMENT '主键id',
  account varchar(32) NULL DEFAULT NULL COMMENT '账号',
  name varchar(32) NULL DEFAULT NULL COMMENT '姓名',
  password varchar(32) NULL DEFAULT NULL COMMENT '密码',
  phone varchar(32) NULL DEFAULT NULL COMMENT '手机号',
  member_card_no varchar(32) NULL DEFAULT NULL COMMENT '会员卡号',
  address varchar(128) NULL DEFAULT NULL COMMENT '地址',
  application_id int(11) NULL DEFAULT NULL COMMENT '应用id',
  create_person varchar(32) NULL DEFAULT NULL COMMENT '创建人',
  create_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '创建时间',
  create_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '创建时间戳',
  update_person varchar(32) NULL DEFAULT NULL COMMENT '更新人',
  update_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '更新时间',
  update_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '更新时间戳',
  dr varchar(255) NULL DEFAULT NULL COMMENT '逻辑删除 0正常  1 逻辑删除',
  PRIMARY KEY (id) USING BTREE,
  UNIQUE INDEX idx_user_account(account) USING BTREE,
  INDEX idx_user_phone(phone) USING BTREE,
  INDEX idx_user_member_card_no(member_card_no) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE demo_sharding_user_8  (
  id bigint(20) NOT NULL COMMENT '主键id',
  account varchar(32) NULL DEFAULT NULL COMMENT '账号',
  name varchar(32) NULL DEFAULT NULL COMMENT '姓名',
  password varchar(32) NULL DEFAULT NULL COMMENT '密码',
  phone varchar(32) NULL DEFAULT NULL COMMENT '手机号',
  member_card_no varchar(32) NULL DEFAULT NULL COMMENT '会员卡号',
  address varchar(128) NULL DEFAULT NULL COMMENT '地址',
  application_id int(11) NULL DEFAULT NULL COMMENT '应用id',
  create_person varchar(32) NULL DEFAULT NULL COMMENT '创建人',
  create_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '创建时间',
  create_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '创建时间戳',
  update_person varchar(32) NULL DEFAULT NULL COMMENT '更新人',
  update_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '更新时间',
  update_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '更新时间戳',
  dr varchar(255) NULL DEFAULT NULL COMMENT '逻辑删除 0正常  1 逻辑删除',
  PRIMARY KEY (id) USING BTREE,
  UNIQUE INDEX idx_user_account(account) USING BTREE,
  INDEX idx_user_phone(phone) USING BTREE,
  INDEX idx_user_member_card_no(member_card_no) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


CREATE TABLE demo_sharding_user_9  (
  id bigint(20) NOT NULL COMMENT '主键id',
  account varchar(32) NULL DEFAULT NULL COMMENT '账号',
  name varchar(32) NULL DEFAULT NULL COMMENT '姓名',
  password varchar(32) NULL DEFAULT NULL COMMENT '密码',
  phone varchar(32) NULL DEFAULT NULL COMMENT '手机号',
  member_card_no varchar(32) NULL DEFAULT NULL COMMENT '会员卡号',
  address varchar(128) NULL DEFAULT NULL COMMENT '地址',
  application_id int(11) NULL DEFAULT NULL COMMENT '应用id',
  create_person varchar(32) NULL DEFAULT NULL COMMENT '创建人',
  create_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '创建时间',
  create_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '创建时间戳',
  update_person varchar(32) NULL DEFAULT NULL COMMENT '更新人',
  update_time datetime(0) NULL DEFAULT CURRENT_TIME COMMENT '更新时间',
  update_time_stamp bigint(20) NULL DEFAULT NULL COMMENT '更新时间戳',
  dr varchar(255) NULL DEFAULT NULL COMMENT '逻辑删除 0正常  1 逻辑删除',
  PRIMARY KEY (id) USING BTREE,
  UNIQUE INDEX idx_user_account(account) USING BTREE,
  INDEX idx_user_phone(phone) USING BTREE,
  INDEX idx_user_member_card_no(member_card_no) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
