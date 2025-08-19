import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

class Student {
    String name, rollNo;
    int[] marks = new int[3];
    double average;
    int highest, lowest;
    String grade;

    Student(String name, String rollNo, int[] marks) {
        this.name = name;
        this.rollNo = rollNo;
        this.marks = marks;
        this.average = (marks[0] + marks[1] + marks[2]) / 3.0;
        this.highest = Math.max(marks[0], Math.max(marks[1], marks[2]));
        this.lowest = Math.min(marks[0], Math.min(marks[1], marks[2]));
        this.grade = getGrade(this.average);
    }

    private String getGrade(double avg) {
        if (avg >= 90) return "A+";
        else if (avg >= 80) return "A";
        else if (avg >= 70) return "B";
        else if (avg >= 60) return "C";
        else if (avg >= 50) return "D";
        else return "F";
    }
}

public class SimpleStudentGrade extends JFrame {

    JLabel nameLabel, rollLabel, m1Label, m2Label, m3Label;
    JTextField nameField, rollField, m1Field, m2Field, m3Field;
    JButton addBtn, cancelMainBtn, deleteBtn, showBtn;
    ArrayList<Student> students = new ArrayList<>();

    public SimpleStudentGrade() {
        setTitle("Student Grade Entry");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(null);
        c.setBackground(Color.pink);

        nameLabel = new JLabel("Name:");
        nameLabel.setBounds(100, 100, 100, 30);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        c.add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(200, 100, 250, 30);
        c.add(nameField);

        rollLabel = new JLabel("Roll No:");
        rollLabel.setBounds(100, 150, 100, 30);
        rollLabel.setFont(new Font("Arial", Font.BOLD, 16));
        c.add(rollLabel);
        rollField = new JTextField();
        rollField.setBounds(200, 150, 250, 30);
        c.add(rollField);

        m1Label = new JLabel("Physics:");
        m1Label.setBounds(100, 200, 100, 30);
        m1Label.setFont(new Font("Arial", Font.BOLD, 16));
        c.add(m1Label);
        m1Field = new JTextField();
        m1Field.setBounds(200, 200, 250, 30);
        c.add(m1Field);

        m2Label = new JLabel("Chemistry:");
        m2Label.setBounds(100, 250, 100, 30);
        m2Label.setFont(new Font("Arial", Font.BOLD, 16));
        c.add(m2Label);
        m2Field = new JTextField();
        m2Field.setBounds(200, 250, 250, 30);
        c.add(m2Field);

        m3Label = new JLabel("Math:");
        m3Label.setBounds(100, 300, 100, 30);
        m3Label.setFont(new Font("Arial", Font.BOLD, 16));
        c.add(m3Label);
        m3Field = new JTextField();
        m3Field.setBounds(200, 300, 250, 30);
        c.add(m3Field);

        addBtn = new JButton("Add Student");
        addBtn.setFont(new Font("Arial", Font.BOLD, 18));
        addBtn.setBounds(200, 370, 180, 40);
        c.add(addBtn);

        cancelMainBtn = new JButton("Cancel");
        cancelMainBtn.setFont(new Font("Arial", Font.BOLD, 18));
        cancelMainBtn.setBounds(400, 430, 180, 40);
        c.add(cancelMainBtn);

        deleteBtn = new JButton("Delete Student");
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 18));
        deleteBtn.setBounds(200, 430, 180, 40);
        c.add(deleteBtn);

        showBtn = new JButton("Show Students");
        showBtn.setFont(new Font("Arial", Font.BOLD, 18));
        showBtn.setBounds(400, 370, 200, 40);
        c.add(showBtn);

        // Cancel Main Button Action
        cancelMainBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit the application?",
                    "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        deleteBtn.addActionListener(e -> deleteStudent());
        showBtn.addActionListener(e -> showStudentDetailsWindow());

        loadStudentsFromFile();

        addBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String roll = rollField.getText().trim();
                int m1 = Integer.parseInt(m1Field.getText().trim());
                int m2 = Integer.parseInt(m2Field.getText().trim());
                int m3 = Integer.parseInt(m3Field.getText().trim());

                if (name.isEmpty() || roll.isEmpty()) {
                    throw new Exception("Fields cannot be empty");
                }

                if (roll.length() != 13) {
                    throw new Exception("Roll Number must be exactly 13 characters long");
                }

                Student student = new Student(name, roll, new int[]{m1, m2, m3});
                students.add(student);
                saveStudentToFile(student);

                JOptionPane.showMessageDialog(this, "Student added successfully!");

                nameField.setText("");
                rollField.setText("");
                m1Field.setText("");
                m2Field.setText("");
                m3Field.setText("");

                showStudentDetailsWindow();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid numbers for marks!", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private void saveStudentToFile(Student s) {
        try (FileWriter fw = new FileWriter("students.csv", true)) {
            fw.write(s.name + "," + s.rollNo + "," + s.marks[0] + "," + s.marks[1] + "," + s.marks[2] + "\n");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving to file");
        }
    }

    private void loadStudentsFromFile() {
        File file = new File("students.csv");
        if (!file.exists())
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String roll = parts[1];
                int m1 = Integer.parseInt(parts[2]);
                int m2 = Integer.parseInt(parts[3]);
                int m3 = Integer.parseInt(parts[4]);
                students.add(new Student(name, roll, new int[]{m1, m2, m3}));
            }
        } catch (Exception e) {
            System.out.println("Error loading students.");
        }
    }

    private void rewriteFile() {
        try (FileWriter fw = new FileWriter("students.csv")) {
            for (Student s : students) {
                fw.write(s.name + "," + s.rollNo + "," + s.marks[0] + "," + s.marks[1] + "," + s.marks[2] + "\n");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating file!");
        }
    }

    private void deleteStudent() {
        String name = JOptionPane.showInputDialog(this, "Enter Student Name to Delete:");
        String roll = JOptionPane.showInputDialog(this, "Enter Roll No to Delete:");

        if (name == null || roll == null || name.trim().isEmpty() || roll.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Roll No cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean removed = students.removeIf(s -> s.name.equalsIgnoreCase(name.trim()) && s.rollNo.equals(roll.trim()));

        if (removed) {
            rewriteFile();
            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showStudentDetailsWindow() {
        JFrame detailsFrame = new JFrame("All Student Details");
        detailsFrame.setSize(900, 500);
        detailsFrame.setLocationRelativeTo(null);
        detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {"Name", "Roll No", "Physics", "Chemistry", "Math", "Average", "Highest", "Lowest", "Grade"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                c.setBackground(Color.YELLOW);
                c.setForeground(Color.BLACK);
                return c;
            }
        };

        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.setRowHeight(25);

        // Increase column width for Name and Roll No
        table.getColumnModel().getColumn(0).setPreferredWidth(200); // Name
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Roll No

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBackground(Color.YELLOW);
        header.setForeground(Color.BLACK);

        for (Student s : students) {
            model.addRow(new Object[]{
                    s.name, s.rollNo,
                    s.marks[0], s.marks[1], s.marks[2],
                    String.format("%.2f", s.average),
                    s.highest, s.lowest, s.grade
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(750, 320));

        JPanel centerPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(173, 216, 230)); // Light blue
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        centerPanel.add(scrollPane);
        detailsFrame.add(centerPanel, BorderLayout.CENTER);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 16));
        cancelBtn.addActionListener(ev -> detailsFrame.dispose());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(cancelBtn);
        detailsFrame.add(bottomPanel, BorderLayout.SOUTH);

        detailsFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new SimpleStudentGrade();
    }
}
