# CS211 Project MyAppeal

## ชื่อทีม com-sci-lore

## 👤 Team Members
| Student ID  | Full Name (Nickname)          | GitHub Username |
|:-------------:|-------------------------------|:-----------------:|
| 6610401934 | Kerdsiri Srijaroen (Tonnam)   | [tonup](https://github.com/tonup) |
| 6610401985 | Chaiyawatt Hnuwattana (Aock)  | [aockgonnabefree](https://github.com/aockgonnabefree) |
| 6610402132 | Bowornrat Tangnararatchakit (Bright) | [thg1rb](https://github.com/thg1rb) |
| 6610405824 | Komchan Wongsirisompong (Karn) | [karnesque](https://github.com/karnesque) |

## 🎥 System Progress Videos
| Session                      |     Due Date      | YouTube Link |
|------------------------------|:-----------------:|:--------------:|
| Progress Update #1           | Aug 9, 2024, 5:00 PM | [Watch here](https://youtu.be/Y-2oY6yPBaU) |
| Progress Update #2           | Sep 6, 2024, 5:00 PM | [Watch here](https://youtu.be/l7R4g-AhKtM) |
| Progress Update #3           | Sep 27, 2024, 5:00 PM | [Watch here](https://youtu.be/IhHnqhBZbiI) |
| Final Project Presentation  | Oct 18, 2024, 5:00 PM | [Watch here](https://youtu.be/t2GLL_iBwXY) |

## 🎓 Objectives

- This project is part of a course project for **01418211 Software Construction** under the Department of Computer Science. This system makes petition management easy and systematic, covering various types of petitions such as general petitions, leave of absence requests, and personal or sick leave requests. Users can submit petitions and check the status of their petitions. 

## 🚀 Features

Features are categorized based on roles, divided into the following 5 roles:

1. **Students** - Users who can submit various types of requests.

2. **Advisers** - Responsible for reviewing and evaluating student requests, with authority to approve requests for forwarding to department staff or to reject requests.

3. **Department Staff** - Responsible for reviewing requests related to their affiliated departments, with options to approve or forward requests to the faculty level, reject requests, and manage information for students and approvers within the department.

4. **Faculty Staff** - Responsible for reviewing and approving requests at the faculty level, with authority to approve or reject these requests, and manage approver information within the faculty.

5. **System Administrators** - Manage and oversee the entire system.

## 🛠️ Tech. Stacks

## 📂 Project Structure

This project uses the MVC (Model-View-Controller) architecture, which consists of: 

- <b>[M]odels</b> - Handle data and business logic of the system.
- <b>[V]iews</b> - Display information to users (User Interface).
- <b>[C]ontrollers</b> - Act as intermediaries between Models and Views.

```markdown
my-appeal/
├── data/
│   ├── appeal-list.csv
│   ├── approver.csv
│   ├── approves-signs
│   ├── departments.csv
│   ├── faculties.csv
│   ├── modify-date.csv
│   ├── profile-images
│   ├── user-manual.pdf
│   └── users/
├── src/
│   └── main/
│       ├── java/
│       │   └── ku/
│       │       ├── cs/
│       │       │   ├── Main.java
│       │       │   ├── controllers/
│       │       │   │   ├── admin/
│       │       │   │   ├── advisor/
│       │       │   │   ├── department/
│       │       │   │   ├── faculty/
│       │       │   │   ├── general/
│       │       │   │   └── student/
│       │       │   ├── cs211671project/
│       │       │   ├── models/
│       │       │   │   ├── appeals/
│       │       │   │   ├── collections/
│       │       │   │   ├── dates/
│       │       │   │   └── persons/
│       │       │   └── services/
│       │       │       ├── datasources/
│       │       │       ├── exceptions/
│       │       │       └── fileutilities/
│       │       └── module-info.java
│       └── resources/
│           ├── fonts/
│           ├── icons/
│           ├── images/
│           └── ku/ 
│               └── cs/
│                   ├── styles/
│                   └── views/
│                       ├── admin/
│                       ├── advisor/
│                       ├── department/
│                       ├── faculty/
│                       ├── general/
│                       └── student/
└── uml/
```

## 🗃️ Installation And Execution

### 🧑🏻‍💻 MacOS (Apple Silicon)

โปรแกรม สำหรับ MacOS Chip Apple Silicon สามารถดาวน์โหลดได้ที่
* release [CS211-project-4.0.0](https://github.com/CS211-671/cs211-project-com-sci-lore/releases) ชื่อไฟล์ ***CS211-project-for-macOS-apple-silicon.zip***  

---

1. ดับเบิ้ลคลิก "CS211-project-for-macOS-apple-silicon.zip"  
   ![Double Click zip file](https://github.com/aockgonnabefree/cs211-java-extended-resource/blob/b587c6bf05a934604a0ad68fd9fd303cec7a783a/clickfile-mac.png)  
2. เข้าไปที่โฟลเดอร์ "CS211-project-for-macOS-apple-silicon"  
   ![extracted](https://github.com/aockgonnabefree/cs211-java-extended-resource/blob/d6abd66ea636bf872942dcb31a4d455818935682/extractedfile-mac.png)  
3. ดับเบิลคลิ้ก "cs211-project.jar"  
   ![in directory](https://github.com/aockgonnabefree/cs211-java-extended-resource/blob/b587c6bf05a934604a0ad68fd9fd303cec7a783a/indirec-mac.png)  
   คุณเปิดโปรแกรมสำเร็จ  
   ![double click program](https://github.com/aockgonnabefree/cs211-java-extended-resource/blob/b587c6bf05a934604a0ad68fd9fd303cec7a783a/success-mac.png)  
   **or**  
   ใช้คำสั่งด้านล่างในโฟลเดอร์ "CS211-project-for-macOS-apple-silicon" เพื่อเปิดโปรแกรมได้
      ```bash
      java -jar cs211-project.jar
      ```

### 👨🏻‍💻 Windows (x86)

---

โปรแกรม สำหรับ Windows สามารถดาวน์โหลดได้ที่ 
* release [CS211-project-4.0.0](https://github.com/CS211-671/cs211-project-com-sci-lore/releases) ชื่อไฟล์ ***CS211-project-for-windows.zip***

---

1. คลิกขวาและแตกไฟล์ "CS211-project-for-windows.zip"  
   ![extracting windows](https://github.com/aockgonnabefree/cs211-java-extended-resource/blob/1c12db3bd2b18cdb1d8f77ef9b96c40247479d8e/extracting-windows.png)
2. เข้าไปที่โฟลเดอร์ "CS211-project-for-windows"  
   ![in folder windows](https://github.com/aockgonnabefree/cs211-java-extended-resource/blob/1c12db3bd2b18cdb1d8f77ef9b96c40247479d8e/extracted-windows.png)  
3. ดับเบิลคลิ้ก "cs211-project.jar"  
   ![click program](https://github.com/aockgonnabefree/cs211-java-extended-resource/blob/1c12db3bd2b18cdb1d8f77ef9b96c40247479d8e/clickprogram.png)  
   เปิดโปรแกรมสำเร็จ  
   ![success window](https://github.com/aockgonnabefree/cs211-java-extended-resource/blob/d6abd66ea636bf872942dcb31a4d455818935682/success-windows.png)  
   **or**  
   ใช้คำสั่งด้านล่างในโฟลเดอร์ "CS211-project-for-windows"
      ```bash
      java -jar cs211-project.jar
      ```  

## 📖 User Manual
[Click on this text](data/user-manual.pdf) to download the user manual, or download it from the "User Manual" button shown on the login page as displayed in the image:

![how to download](https://github.com/aockgonnabefree/cs211-java-extended-resource/blob/d6abd66ea636bf872942dcb31a4d455818935682/how-to.png)

## 🔐 Mock Users
| Role                                                     | Username  | Password   |
|----------------------------------------------------------|:-----------:|:------------:|
| System Administrator                                              | admin     | 1234567890 |
| Faculty Staff (Science)                             | karn      | 12345678   |
| Department Staff (Computer Science)                 | ton       | 12345678   |
| Academic Adviser (Computer Science)                   | BrightPro | 12345678   |
| Academic Advisor using the system for the first time (Economics) | adv-ec01  | ec-adv01    |
| Student (Computer Science)                           | BrightStu | student    |
