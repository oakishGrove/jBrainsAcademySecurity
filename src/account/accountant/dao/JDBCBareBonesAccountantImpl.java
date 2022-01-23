package account.accountant.dao;

import account.dtos.PayRollDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class JDBCBareBonesAccountantImpl implements AccountantDao {
    private final DataSource dataSource;

    private static final String AddPaymentSQL = "INSERT INTO payroll (id, salary, period, employee_id )" +
            " VALUES (payroll_sq.nextval, ?,?,?);";
    private static final String findEmployeeSQL = "SELECT id, email FROM user_details AS u WHERE u.email = ?;";
    private static final String uniquePeriodSQL = "SELECT id FROM payroll AS pe WHERE pe.period = ? AND pe.employee_id = ? LIMIT 1;";
    private static final String fetchPayrollByUserIdAndPeriod = "SELECT * FROM payroll AS p WHERE p.employee_id = ? AND p.period = ?";
    private static final String selectAllPayrolls = "SELECT * FROM payroll WHERE payroll.employee_id = ?";

    @Override
    public void uploadPayrolls(List<PayRollDto> payRollDtoList) {
        try (var connection = dataSource.getConnection()) {

            connection.setAutoCommit(false);

            for (var payment : payRollDtoList) {
                addPaymentMethod(connection, payment);
            }

            connection.commit();
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update salaries");
        }
    }

    @Override
    public PayRollEntity getPayroll(Long userId, LocalDate period) {
        try (var connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);

            try (var fetchPayrollStatement = connection.prepareStatement(fetchPayrollByUserIdAndPeriod)) {

                fetchPayrollStatement.setLong(1, userId);
                fetchPayrollStatement.setString(2, period.toString());
                var resultSet = fetchPayrollStatement.executeQuery();

                if (resultSet.next()) {
                    PayRollEntity entity = new PayRollEntity();
                    entity.setId(resultSet.getLong("id"));
                    entity.setPeriod(LocalDate.parse(resultSet.getString("period")));
                    entity.setSalary(resultSet.getLong("salary"));
                    return entity;
                }
                resultSet.close();
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to retrieve payroll");
        }
        return null;
    }

    @Override
    public List<PayRollEntity> getPayrolls(Long userId) {
        ArrayList<PayRollEntity> payRollEntities = new ArrayList<>();

        try (var connection = dataSource.getConnection();
            var fetchAllPayrolls = connection.prepareStatement(selectAllPayrolls)
        ) {
            fetchAllPayrolls.setLong(1, userId);
            try (var set = fetchAllPayrolls.executeQuery()) {

                while (set.next()) {
                    var entity = new PayRollEntity();
                    entity.setSalary(set.getLong("salary"));
                    entity.setId(set.getLong("id"));
                    entity.setPeriod(LocalDate.parse(set.getString("period")));

                    payRollEntities.add(0, entity);
                }
                System.out.println(set);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return payRollEntities;
    }

    private void addPaymentMethod(Connection connection, PayRollDto payment) throws SQLException {
        try (PreparedStatement addPayment = connection.prepareStatement(AddPaymentSQL, Statement.RETURN_GENERATED_KEYS)) {

            Long userId = getUserId(connection, payment.getEmployee());
            if (userId == null) {
                rollback(connection, "User can't add to user");
            }

            if (isPeriodUnique(connection, payment.getPeriod(), userId)) {
                rollback(connection, "Period is not unique");
            }

            addPayment.setLong(1, payment.getSalary());
            addPayment.setString(2, payment.getPeriod().toString());
            addPayment.setLong(3, userId);
            addPayment.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            rollback(connection, "Failed to complete salaries update");
        }
    }

    public void updatePayroll(PayRollDto payroll) {
        try (var connection = dataSource.getConnection()) {
            final String updateSQL = "UPDATE payroll SET salary = ? WHERE period = ? AND employee_id = ?";
            try (PreparedStatement addPayment = connection.prepareStatement(updateSQL, Statement.RETURN_GENERATED_KEYS)) {

                Long userId = getUserId(connection, payroll.getEmployee());
                if (userId == null) {
                    rollback(connection, "User can't add to user");
                }

                addPayment.setLong(1, payroll.getSalary());
                addPayment.setString(2, payroll.getPeriod().toString());
                addPayment.setLong(3, userId);
                addPayment.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
                rollback(connection, "Failed to complete salaries update");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FAILED TO UPDATE");
        }
    }

    private boolean isPeriodUnique(Connection connection, LocalDate period, Long userId) throws SQLException {
        try (var uniquePeriodStatement = connection.prepareStatement(uniquePeriodSQL)) {
            uniquePeriodStatement.setString(1, period.toString());
            uniquePeriodStatement.setLong(2, userId);
            var resultSet =  uniquePeriodStatement
                    .executeQuery();
            boolean hasNext = resultSet.next();
            resultSet.close();
            return hasNext;
        } catch (SQLException exception) {
            rollback(connection, "SQL Error while requesting unique period");
        }
        return false;
    }

    private Long getUserId(Connection connection, String userEmail) throws SQLException {
        try (PreparedStatement findEmployee = connection.prepareStatement(findEmployeeSQL)) {
            findEmployee.setString(1, userEmail);
            var resultSet = findEmployee.executeQuery();

            if (resultSet.next()) {
                long retVal = resultSet.getLong("id");
                resultSet.close();
                return retVal;
            }
        } catch (SQLException exception) {
            rollback(connection, "SQL Error while requesting unique period");
        }
        return null;
    }

    private void rollback(Connection connection, String message) throws SQLException {
        connection.rollback();
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
