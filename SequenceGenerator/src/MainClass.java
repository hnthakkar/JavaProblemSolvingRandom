import java.util.Scanner;


public class MainClass {
    
    public static void main(String[] arg){
        AVLTree avl = new AVLTree();
        int choice = 0;
        Scanner scan = null;
        do{
            System.out.println("To insert a Node Press 1");
            System.out.println("To delete a Node Press 2");
            System.out.println("To print the current tree Press 3");
            System.out.println("To exit Press 4");
            scan = new Scanner(System.in);
            choice = scan.nextInt();
            int value = 0;
            switch(choice){
                case 1:
                    System.out.println("Please enter the value of new Node");
                    value = scan.nextInt();
                    avl.add(value);
                    avl.getView();
                    break;
                case 2:
                    System.out.println("Please enter the value to delete");
                    value = scan.nextInt();
                   // avl.remove(value);
                    break;
                case 3:
                   // System.out.println(avl.getPreOrder());
                   // System.out.println(avl.getInOrder());
                    break;
                case 4:
                    break;
                    
            }
        }while(choice != 4);
        scan.close();
    }
}
