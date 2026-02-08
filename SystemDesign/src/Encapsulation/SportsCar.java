package Encapsulation;

public class SportsCar {

    String model;
    String brand;
    private int speed = 0;
    boolean isEngineOn = false;

    public SportsCar(String model, String brand) {
        this.model = model;
        this.brand = brand;
    }

    public void start() {
        this.isEngineOn=true;
        System.out.println("Engine on is :" + this.isEngineOn);
    }

    public void stop() {
        this.isEngineOn=false;
        this.speed = 0;
        System.out.println("Engine on is :" + this.isEngineOn + " and speed is :" + this.speed);
    }

    public void accelerate() {
        if(this.isEngineOn){
            this.speed = this.speed+20;
            System.out.println("Speed is :" + this.speed);
        }

    }

    public void brake() {

        this.speed = this.speed-5;
        System.out.println("Speed is :" + this.speed);

    }
}
