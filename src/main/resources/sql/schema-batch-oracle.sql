CREATE TABLE BATCH_JOB_INSTANCE (
	JOB_INSTANCE_ID NUMBER(19,0)  NOT NULL PRIMARY KEY,
	VERSION NUMBER(19,0),
	JOB_NAME VARCHAR2(100 char) NOT NULL,
	JOB_KEY VARCHAR2(32 char) NOT NULL,
	constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) SEGMENT CREATION IMMEDIATE;

CREATE TABLE BATCH_JOB_EXECUTION (
	JOB_EXECUTION_ID NUMBER(19,0)  NOT NULL PRIMARY KEY,
	VERSION NUMBER(19,0),
	JOB_INSTANCE_ID NUMBER(19,0) NOT NULL,
	CREATE_TIME TIMESTAMP(9) NOT NULL,
	START_TIME TIMESTAMP(9) DEFAULT NULL,
	END_TIME TIMESTAMP(9) DEFAULT NULL,
	STATUS VARCHAR2(10 char),
	EXIT_CODE VARCHAR2(2500 char),
	EXIT_MESSAGE VARCHAR2(2500 char),
	LAST_UPDATED TIMESTAMP(9),
	constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
	references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) SEGMENT CREATION IMMEDIATE;

CREATE TABLE BATCH_JOB_EXECUTION_PARAMS (
	JOB_EXECUTION_ID NUMBER(19,0) NOT NULL,
	PARAMETER_NAME VARCHAR(100 char) NOT NULL,
	PARAMETER_TYPE VARCHAR(100 char) NOT NULL,
	PARAMETER_VALUE VARCHAR(2500 char),
	IDENTIFYING CHAR(1) NOT NULL,
	constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) SEGMENT CREATION IMMEDIATE;

CREATE TABLE BATCH_STEP_EXECUTION (
	STEP_EXECUTION_ID NUMBER(19,0)  NOT NULL PRIMARY KEY,
	VERSION NUMBER(19,0) NOT NULL,
	STEP_NAME VARCHAR2(100 char) NOT NULL,
	JOB_EXECUTION_ID NUMBER(19,0) NOT NULL,
	CREATE_TIME TIMESTAMP(9) NOT NULL,
	START_TIME TIMESTAMP(9) DEFAULT NULL,
	END_TIME TIMESTAMP(9) DEFAULT NULL,
	STATUS VARCHAR2(10 char),
	COMMIT_COUNT NUMBER(19,0),
	READ_COUNT NUMBER(19,0),
	FILTER_COUNT NUMBER(19,0),
	WRITE_COUNT NUMBER(19,0),
	READ_SKIP_COUNT NUMBER(19,0),
	WRITE_SKIP_COUNT NUMBER(19,0),
	PROCESS_SKIP_COUNT NUMBER(19,0),
	ROLLBACK_COUNT NUMBER(19,0),
	EXIT_CODE VARCHAR2(2500 char),
	EXIT_MESSAGE VARCHAR2(2500 char),
	LAST_UPDATED TIMESTAMP(9),
	constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) SEGMENT CREATION IMMEDIATE;

CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT (
	STEP_EXECUTION_ID NUMBER(19,0) NOT NULL PRIMARY KEY,
	SHORT_CONTEXT VARCHAR2(2500 char) NOT NULL,
	SERIALIZED_CONTEXT CLOB,
	constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
	references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) SEGMENT CREATION IMMEDIATE;

CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT (
	JOB_EXECUTION_ID NUMBER(19,0) NOT NULL PRIMARY KEY,
	SHORT_CONTEXT VARCHAR2(2500 char) NOT NULL,
	SERIALIZED_CONTEXT CLOB,
	constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) SEGMENT CREATION IMMEDIATE;

CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 ORDER NOCYCLE;
CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 ORDER NOCYCLE;
CREATE SEQUENCE BATCH_JOB_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 ORDER NOCYCLE;
