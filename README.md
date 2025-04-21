# Exam Management system : OOAD Project :

# How to run and Workflow Instructions : 

Run : ooad project/exam-management-system/src/main/java/com/exam/exam_management_system/ExamManagementSystemApplication.java
## Step-by-Step Guide

1. ### Teacher Registration
   - Open the login screen and click **"Register"**.
   - Register a new user with the role **Teacher**.

2. ### Exam Coordinator Validation
   - Login as Exam Coordinator:
     - **Username**: `exam_coordinator`
     - **Password**: `coordinator123`
   - Validate the newly registered teacher.

3. ### Paper Creation and Submission by Teacher
   - Login as the registered **Teacher**.
   - Create an exam paper.
   - Submit the paper for proofreading.

4. ### Proofreading by Another Teacher
   - Logout and register/login as another **Teacher**.
   - Proofread the submitted paper.

5. ### Setting the Paper Online by Controller of Examination
   - Login as Controller of Examination:
     - **Username**: `exam_controller`
     - **Password**: `controller123`
   - Set the proofread paper online.

6. ### Student Registration for Exam
   - Login as Student:
     - **Username**: `stu`
     - **Password**: `abc`
   - Register for the exam.

7. ### Changing the Schedule by Exam Coordinator
   - Login again as **Exam Coordinator**.
   - Modify the exam schedule.

8. ### Student Attempting the Exam
   - Login back as **Student**.
   - Attempt the scheduled exam.

---

## Default Test Credentials

| Role                  | Username            | Password         |
|-----------------------|---------------------|------------------|
| Teacher (default)     | `tea`               | `abc`            |
| Student (default)     | `stu`               | `abc`            |
| Exam Coordinator      | `exam_coordinator`  | `coordinator123` |
| Controller of Exam    | `exam_controller`   | `controller123`  |

---

> Ensure each role performs the required actions in sequence to simulate the full exam lifecycle.


