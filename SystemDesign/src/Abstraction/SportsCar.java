package Abstraction;

public class SportsCar implements Car{
    String model;
    String brand;
    int speed = 0;
    boolean isEngineOn = false;

    public SportsCar(String model, String brand) {
        this.model = model;
        this.brand = brand;
    }

    @Override
    public void start() {
        this.isEngineOn=true;
        System.out.println("Engine on is :" + this.isEngineOn);
    }

    @Override
    public void stop() {
        this.isEngineOn=false;
        this.speed = 0;
        System.out.println("Engine on is :" + this.isEngineOn + " and speed is :" + this.speed);
    }

    @Override
    public void accelerate() {
        if(this.isEngineOn){
            this.speed = this.speed+20;
            System.out.println("Speed is :" + this.speed);
        }

    }

    @Override
    public void brake() {

        this.speed = this.speed-5;
        System.out.println("Speed is :" + this.speed);

    }
}
