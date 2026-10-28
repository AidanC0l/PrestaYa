package main.java.com.prestaya.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

//Se usa record para almacenar datos de manera concisa y se implementa la interfaz ApplicationStatus
public record ApprovedStatus(BigDecimal approvedAmount, BigDecimal calculatedInstallment) implements ApplicationStatus {

    public ApprovedStatus {
        //Se validan los valores
        if (approvedAmount == null || approvedAmount.compareTo(BigDecimal.ZERO) <0) {

            throw new IllegalArgumentException("El monto aprobado debe ser mayor que cero");

        }

        if (calculatedInstallment == null || calculatedInstallment.compareTo(BigDecimal.ZERO) <=0) {
            
            throw new IllegalArgumentException("La cuota mensual debe ser mayor que cero");
        
        }

        //Se redondean los valores
        approvedAmount = approvedAmount.setScale(2, RoundingMode.HALF_UP);

        calculatedInstallment = calculatedInstallment.setScale(2, RoundingMode.HALF_UP);

    }
    
}
