package org.opensourceframework.component.dao.transaction;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.opensourceframework.component.dao.contant.SqlStatementContant.*;

/**
 * 分布式数据库事务管理
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class DistributedTransactionManager extends DataSourceTransactionManager {
	private static final long serialVersionUID = -8925425663858863783L;

	private Boolean distributedFlag = false;
	private String transactionPolicy = POLICY_FLEXIBLE;

	public Boolean isDistributedFlag() {
		return distributedFlag;
	}

	public void setDistributedFlag(Boolean distributedFlag) {
		this.distributedFlag = distributedFlag;
	}

	public DistributedTransactionManager(DataSource dataSource) {
		super(dataSource);
	}

	public DistributedTransactionManager(DataSource dataSource, Boolean distributedFlag) {
		super(dataSource);
		this.distributedFlag = distributedFlag;
	}

	public DistributedTransactionManager(DataSource dataSource, Boolean distributedFlag , String transactionPolicy) {
		super(dataSource);
		this.distributedFlag = distributedFlag;
		this.transactionPolicy = transactionPolicy;
	}

	@Override
	protected void prepareTransactionalConnection(Connection con , TransactionDefinition definition) throws SQLException {
		if (!this.distributedFlag) {
			super.prepareTransactionalConnection(con, definition);
			return;
		}
		Statement stmt = con.createStatement();

		String transPolicy = this.transactionPolicy.toUpperCase();

		try {
			switch (transPolicy) {
				case POLICY_FREE:
					stmt.executeUpdate("SET drds_transaction_policy = 'FREE'");
					break;
				case POLICY_TWO_PC:
					stmt.executeUpdate("SET drds_transaction_policy = '2PC'");
					break;
				case POLICY_XA:
					stmt.executeUpdate("SET drds_transaction_policy = 'XA'");
					break;
				case POLICY_FLEXIBLE:
				default:
					stmt.executeUpdate("SET drds_transaction_policy = 'FLEXIBLE'");
			}
			super.prepareTransactionalConnection(con, definition);
		} catch (Throwable throwable) {
			throw throwable;
		} finally {
			stmt.close();
		}
	}
}
