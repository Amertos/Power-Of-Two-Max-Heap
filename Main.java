import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 1. KONTROLA INTERFEJSA
class ConsoleUI {
    public static final String RESET = "\033[0m";
    public static final String RED = "\033[1;31m";    // Za Vatrogasce
    public static final String BLUE = "\033[1;34m";   // Za Policiju
    public static final String CYAN = "\033[1;36m";   // Hitna
    public static final String YELLOW = "\033[1;33m"; // Komunalna
    public static final String GREEN = "\033[1;32m";  // Za krajnji uspjeh

    public static void sleep(int millis) {
        try { Thread.sleep(millis); }
        catch (InterruptedException ignored) {}
    }

    public static void printBanner() {
        System.out.println(CYAN + "\n==============================================");
        System.out.println("   🚀 MISSION DISPATCHER SYSTEM PRO 🚀   ");
        System.out.println("==============================================" + RESET);
    }
}

// 2. KATEGORIZACIJA MISIJA (Enumi)
enum MissionType {
    FIRE(ConsoleUI.RED, "Vatrogasci"),
    MEDICAL(ConsoleUI.CYAN, "Hitna Pomoc"),
    POLICE(ConsoleUI.BLUE, "Specijalne Jedinice"),
    GENERAL(ConsoleUI.YELLOW, "Komunalna Redarstva");

    private final String color;
    private final String responder;

    MissionType(String color, String responder) {
        this.color = color;
        this.responder = responder;
    }
    public String getColor() { return color; }
    public String getResponder() { return responder; }
}

// 3. OBOGAĆENI DOMENSKI MODEL MISIJE
class Mission implements Comparable<Mission> {
    private final String id;
    private final String description;
    private final int priority;
    private final MissionType type;
    private final LocalDateTime createdAt;

    public Mission(String description, int priority, MissionType type) {
        if (description == null || description.isEmpty()) throw new IllegalArgumentException("Opis ne moze biti prazan.");

        this.id = UUID.randomUUID().toString().substring(0, 5).toUpperCase(); // Kratki unikatni kod
        this.description = description;
        this.priority = priority;
        this.type = type;
        this.createdAt = LocalDateTime.now(); // Beležimo tačno vrijeme!
    }

    public int getPriority() { return priority; }
    public MissionType getType() { return type; }

    public String getFormattedOutput() {
        String time = createdAt.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return String.format("%s[%s] [PR:%03d] [ID:%s] %s%s",
                type.getColor(), time, priority, id, description, ConsoleUI.RESET);
    }

    @Override
    public int compareTo(Mission other) {
        // Ako je prioritet identičan, ona misija koja je ranije napravljena rješava se prva (FIFO)
        if (this.priority == other.priority) {
            return other.createdAt.compareTo(this.createdAt);
        }
        return Integer.compare(this.priority, other.priority);
    }
}

// 4. GENERIČKA STRUKTURA HEAP-a (Ovo je najbitniji korak za prvu klasu koda)
// T extends Comparable<T> znaci da ovaj Heap sada može primiti BILO KOJU klasu, dok god se može porediti.
class PowerOfTwoMaxHeap<T extends Comparable<T>> {
    private Object[] heap;
    private int size;
    private final int k;
    private final int numChildren;

    public PowerOfTwoMaxHeap(int initialCapacity, int k) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("Kapacitet mora > 0");
        if (k < 1) throw new IllegalArgumentException("k mora >= 1");

        this.heap = new Object[initialCapacity];
        this.size = 0;
        this.k = k;
        this.numChildren = 1 << k;
    }

    public boolean isEmpty() { return size == 0; }

    public void insert(T item) {
        if (item == null) throw new IllegalArgumentException("Item je null");
        if (size == heap.length) heap = Arrays.copyOf(heap, heap.length * 2);

        heap[size] = item;
        siftUp(size);
        size++;
    }

    @SuppressWarnings("unchecked")
    public T extractMax() {
        if (isEmpty()) throw new NoSuchElementException("Prazno!");
        T maxItem = (T) heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;

        if (size > 0) siftDown(0);
        return maxItem;
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int index) {
        T temp = (T) heap[index];
        int parentIndex = (index - 1) >> k;
        while (index > 0 && ((T) heap[parentIndex]).compareTo(temp) < 0) {
            heap[index] = heap[parentIndex];
            index = parentIndex;
            parentIndex = (index - 1) >> k;
        }
        heap[index] = temp;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int index) {
        T temp = (T) heap[index];
        while (true) {
            int largestChildIndex = -1;
            T maxItem = temp;
            int firstChildIndex = (index << k) + 1;

            for (int i = 0; i < numChildren; i++) {
                int currentChild = firstChildIndex + i;
                if (currentChild >= size) break;

                if (((T) heap[currentChild]).compareTo(maxItem) > 0) {
                    maxItem = (T) heap[currentChild];
                    largestChildIndex = currentChild;
                }
            }

            if (largestChildIndex == -1) break;

            heap[index] = heap[largestChildIndex];
            index = largestChildIndex;
        }
        heap[index] = temp;
    }
}

// 5. BIZNIS KONTROLER (Odvajanje logike od Main-a)
class MissionDispatcher {
    private final PowerOfTwoMaxHeap<Mission> queue;

    public MissionDispatcher(int k) {
        this.queue = new PowerOfTwoMaxHeap<>(10, k);
    }

    public void receiveMission(Mission m) {
        queue.insert(m);
        System.out.println(" 📥 Primljeno: " + m.getFormattedOutput());
        ConsoleUI.sleep(300);
    }

    public void startDispatching() {
        System.out.println(ConsoleUI.CYAN + "\n==============================================");
        System.out.println("   🚨 POKRETANJE HITNIH AKCIJA 🚨   ");
        System.out.println("==============================================" + ConsoleUI.RESET);
        ConsoleUI.sleep(1000);

        while (!queue.isEmpty()) {
            Mission current = queue.extractMax();
            System.out.println("\n ⚙️ Dispecer salje tim na teren: " + current.getType().getResponder() + "...");
            ConsoleUI.sleep(600);
            System.out.println(" ✅ RIJESENO: " + current.getFormattedOutput());
            ConsoleUI.sleep(600);
        }

        System.out.println(ConsoleUI.GREEN + "\n🎉 SVI ZADACI SU USPJESNO RIJESENI! Sistem prelazi u standby...\n" + ConsoleUI.RESET);
    }
}

// 6. MAIN (Ovo mora biti izuzetno kratko i cisto)
public class Main {
    public static void main(String[] args) {
        ConsoleUI.printBanner();

        MissionDispatcher dispatcher = new MissionDispatcher(2); // k=2 (4-ary heap)

        System.out.println("\n📡 Uspostavljanje veze sa terenom...\n");
        ConsoleUI.sleep(800);

        // Simulacija primanja misija
        dispatcher.receiveMission(new Mission("Macka na drvetu", 10, MissionType.GENERAL));
        dispatcher.receiveMission(new Mission("Pljacka banke u toku", 90, MissionType.POLICE));
        dispatcher.receiveMission(new Mission("Sudar na raskrsnici", 75, MissionType.MEDICAL));
        dispatcher.receiveMission(new Mission("Pozar u neboderu", 100, MissionType.FIRE));
        dispatcher.receiveMission(new Mission("Prijavljen sumnjiv paket", 85, MissionType.POLICE));
        dispatcher.receiveMission(new Mission("Zalba na glasnu muziku", 5, MissionType.GENERAL));

        // Akcija
        dispatcher.startDispatching();
    }
}
