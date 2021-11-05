public class Test {
  public static void main(String[] args) throws  Exception {
      new Test().doWork(new Callback() { // implementing class            
          @Override
          public void call() {
              System.out.println("DB CRUD");
          }
      });
  }

  public void doWork(Callback callback) {
      System.out.println("DB connect");
      callback.call();
      System.out.println("DB close");
  }

  public interface Callback {
      void call();
  }
}
