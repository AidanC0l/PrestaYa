package main.java.com.prestaya.model;

public record Customer(String id, String name, int creditScore, String city) {
    public Customer {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El identificador del cliente es obligatorio.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }
        if (creditScore < 0 || creditScore > 1000) {
            throw new IllegalArgumentException("El puntaje crediticio debe estar entre 0 y 1000.");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("La ciudad del cliente es obligatoria.");
        }
        
        id = id.trim();
        name = name.trim();
        city = city.trim();

    }
}