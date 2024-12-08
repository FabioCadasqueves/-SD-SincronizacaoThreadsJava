class MeuDadoEvent {
    private int Dado;
    private boolean Pronto;

    public MeuDadoEvent() {
        Pronto = false;
    }

    public synchronized void armazenar(int Data) {
        while (Pronto) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        this.Dado = Data;
        Pronto = true;
        notify();
    }

    public synchronized int carregar() {
        while (!Pronto) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        Pronto = false;
        notify();
        return this.Dado;
    }
}

class ProdutorEvent implements Runnable {
    MeuDadoEvent dado;

    public ProdutorEvent(MeuDadoEvent dado) {
        this.dado = dado;
    }

    public void run() {
        for (int i = 0; i < 30; i++) {
            dado.armazenar(i);
            System.out.println("Produtor usando Eventos: " + i);
            try {
                Thread.sleep((int) (Math.random() * 500));
            } catch (InterruptedException e) { }
        }
    }
}

class ConsumidorEvent implements Runnable {
    MeuDadoEvent dado;

    public ConsumidorEvent(MeuDadoEvent dado) {
        this.dado = dado;
    }

    public void run() {
        for (int i = 0; i < 30; i++) {
            System.out.println("Consumidor usando Eventos: " + dado.carregar());
            try {
                Thread.sleep((int) (Math.random() * 500));
            } catch (InterruptedException e) { }
        }
    }
}

class MeuDadoEventJava {
    public static void main(String[] argv) {
        MeuDadoEvent dado = new MeuDadoEvent();
        new Thread(new ProdutorEvent(dado)).start();
        new Thread(new ConsumidorEvent(dado)).start();
    }
}
