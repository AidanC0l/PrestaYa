package main.java.com.prestaya.model;

//Su atributo rejectionReason almacena la explicación del rechazo
public record RejectedStatus(String rejectionReason) implements ApplicationStatus {
    
    public RejectedStatus {

        if (rejectionReason == null || rejectionReason.isBlank()) {
            
            throw new IllegalArgumentException("El motivo del rechazo es obligatorio");
        
        }

        //Se eliminan los espacios innecesarioa al inicio y final del texto
        rejectionReason = rejectionReason.trim();

    }


}
