package main.java.com.prestaya.model;

    import java.math.BigDecimal;
    import java.math.RoundingMode;

//Se usa la palabra abstract para indicar que es una clase incompleta y servirá como modelo para otras clases.
//Se usa la abstracción ya que no se quiere duplicar en cada producto financiero los datos y métodos que los créditos comparten.
public abstract class Loan {
    
//Se usan los atributos como fina porque los valores se van a establecer al cosntruir el credito y ya despues no se podrá reasignar.
    private final String loanId;
    private final BigDecimal amount;
    private final int termMonths;
    private final BigDecimal interestRate;

    private BigDecimal monthlyInstallment;

//Se crea el constructor con los datos necesarios para crear un credito y se usa el protected para que el usuario no cree instancias de la clase abstracta
    protected Loan(String loanId, BigDecimal amount, int termMonths, BigDecimal interestRate) {
    
    if (loanId == null || loanId.isBlank()) {
        throw new IllegalArgumentException("El indentificador del crédito es obligatorio.");
    }
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("El monto del crédito debe ser mayor que cero.");
    }
    if (termMonths <= 0) {
        throw new IllegalArgumentException("El plazo debe ser mayor que cero meses.");
    }
    if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) < 0) {
        throw new IllegalArgumentException("La tasa de interés no puede ser negativa.");
    }

    this.loanId = loanId.trim();
    this.amount = amount.setScale(2, RoundingMode.HALF_UP); //Se usa para redondearla cantidad monetaria
    this.termMonths = termMonths;
    this.interestRate = interestRate;

    }

    public String getLoanId() {
        return loanId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getTermMonths() {
        return termMonths;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

//Se crea el metodo que permite que las subclases registren la cuota calculada
    protected final void setMonthlyInstallment(BigDecimal monthlyInstalmennt) {
        if (monthlyInstallment == null || monthlyInstallment.compareTo(BigDecimal.ZERO) < 0 ) {
            throw new IllegalArgumentException("La cuota mensual no puede ser negativa ni nula.");
        }

        this.monthlyInstallment = monthlyInstallment.setScale(2, RoundingMode.HALF_UP);
        
    }

//Se declara el metodo abstracto
    public abstract BigDecimal calculateMonthlyInstallment();

//Se sobreescribe el metodo toString y se define cómo se representará el crédito cuando lo imprimamos en la consola
    @Override
    public String toString() {
        return """
                Crédito: %s
                Monto financiado: $s%%
                Plazo: %d meses
                Tasa mensual: %s%%
                Cuota mensual: %s
                """. formatted(
                loanId,
                amount.toPlainString(),
                termMonths,
                interestRate.multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString(),
                monthlyInstallment == null 
                ? "Pendiente de cálculo"
                : monthlyInstallment.toPlainString());
    }

}
