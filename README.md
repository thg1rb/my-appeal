<center>
    <h1>🏫 CS211 Project MyAppeal</h1>
    <p>
        This Appeal Management System was developed as part of Course <b>01418211 - Software Construction</b> in the Department of Computer Science.
    </p>
</center>

## 👤 Team Members (com-sci-lore)
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

## 📸 Screenshots

![screenshot1](https://github.com/user-attachments/assets/449a3e25-da56-4287-8dd6-506671c8c112)

![screenshot2](https://github.com/user-attachments/assets/af1b248d-1a69-4017-88f8-7c544c3a32e1)

![screenshot3](https://github.com/user-attachments/assets/5a31d1a0-26b9-44a3-aac9-a466015ae949)

![screenshot4](https://github.com/user-attachments/assets/daf8c5bb-7868-4c6f-a154-bac04c55e154)

![screenshot5](https://github.com/user-attachments/assets/8308d001-59f0-43a1-8adc-6851606ac274)

![screenshot6](https://github.com/user-attachments/assets/cdf75ace-2041-40f7-a9cf-3004b4fdf2dd)

## 🛠️ Tech. Stacks

- **Framework**: JavaFX
- **Language**: Java
- **UI Design**: Figma, Scene Builder

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

## 🗃️ Installation & Running Guide

### 🍎 MacOS (Apple Silicon)

1. Download the [CS211-project-for-MacOS-apple-silicon.zip](https://github.com/thg1rb/my-appeal/releases/download/4.0.0/CS211-project-for-MacOS-apple-silicon.zip).
2. Extract the `.zip` file to get a folder named `CS211-project-for-MacOS-apple-silicon`.
3. Locate the `cs211-project.jar` file inside the folder.
4. Run the program by either:

   - Double-clicking the `.jar` file, or

   - Using terminal command:
    ```bash
    java -jar cs211-project.jar
    ```


### 🪟 Windows (x86)

1. Download the [CS211-project-for-Windows.zip](https://github.com/thg1rb/my-appeal/releases/download/4.0.0/CS211-project-for-Windows.zip).
2. Extract the `.zip` file to get a folder named `CS211-project-for-Windows`.
3. Locate the `cs211-project.jar` file inside the folder.
4. Run the program by either:

   - Double-clicking the `.jar` file, or

   - Using terminal command:
    ```bash
    java -jar cs211-project.jar
    ```

## 📖 User Manual
- [Click on this text](data/user-manual.pdf) to download the user manual.
- Or download it from the "User Manual" button shown on the login page as displayed in the image:

![manual](https://github.com/user-attachments/assets/f6f7a530-8c6a-49f1-9c01-6d7d2e92e482)

## 🔐 Mock Users
| Role                                                     | Username  | Password   |
|----------------------------------------------------------|:-----------:|:------------:|
| System Administrator                                              | admin     | 1234567890 |
| Faculty Staff (Science)                             | karn      | 12345678   |
| Department Staff (Computer Science)                 | ton       | 12345678   |
| Academic Adviser (Computer Science)                   | BrightPro | 12345678   |
| Academic Advisor using the system for the first time (Economics) | adv-ec01  | ec-adv01    |
| Student (Computer Science)                           | BrightStu | student    |
