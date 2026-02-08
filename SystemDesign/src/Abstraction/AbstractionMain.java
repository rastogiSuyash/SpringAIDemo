package Abstraction;

public class AbstractionMain {

    public static void main(String[] args) {
        Car car = new SportsCar("V5","BMW");
        car.start();
        car.accelerate();
        car.brake();
        car.stop();
    }
}
