package com.encore.hms.service;

import com.encore.hms.domain.EmployeeDto;
import com.encore.hms.domain.StudentDto;
import com.encore.hms.domain.TeacherDto;
import com.encore.hms.domain.sup.Person;
import com.encore.hms.util.HmsType;
import lombok.Getter;

import java.io.*;

// business logic 가지고 있는 클래스
// 1. 배열을 관리
// 2. 생성된 배열에 학생, 강사, 직원 객체를 담을 것
// 3. 배열의 인덱스를 이용해서 찾기, 수정, 삭제
@Getter
public class HmsService {

    private Person[] personArray;
    private int index;

    public HmsService() {
    }

    public HmsService(int size) {
        personArray = new Person[size];
    }

    public String makePerson(HmsType type, String name, int age, String address, String detail) {
        String msg = null;
        Person person = switch (type) {
            case STUDENT -> new StudentDto(name, age, address, detail);
            case TEACHER -> new TeacherDto(name, age, address, detail);
            case EMPLOYEE -> new EmployeeDto(name, age, address, detail);
        };

        setPersonArray(person);
        msg = (index) + "번지에 데이터를 담았습니다.";
        return msg;
    }

    public void setPersonArray(Person person) {
        personArray[index++] = person;
    }

    public Person findByName(String inputName) {
        Person person = null;
        for (int i = 0; i < personArray.length; i++) {
            person = personArray[i];
            if (person != null) {
                if (person.getName().equals(inputName)) {
                    return person;
                }
            }
        }
        return null;
    }

    // 1. 배열객체가 가지고 있는 ary.clone();
    // 2. Arrays.copyOf(ary, ary.length)
    public Person updatePerson(String findName) {
       /* Person[] copyPerson = personArray.clone();
        Person copyPerson = Arrays.copyOf(ary, ary.length);
        System.out.println("original address : " + personArray);
        System.out.println("copy address : " + copyPerson);*/
        return findByName(findName);
    }

    /*public Person updatePerson(String findName, String inputChangeDetail) {
        Person person = findByName(findName);
        if (person instanceof StudentDto student) {
            student.changeStudentId(inputChangeDetail);
        } else if (person instanceof EmployeeDto employee) {
            employee.changeDept(inputChangeDetail);
        } else if (person instanceof TeacherDto teacher) {
            teacher.changeSubject(inputChangeDetail);
        }
        return person;
    }*/

    public void remove(String findName) {
        int count = personArray.length;
        boolean flag = false;
        for (int i = 0; i < count; i++) {
            if (findName.equals(personArray[i].getName())) {
                System.out.println(personArray[i] + "를 삭제했습니다.");
                for (int j = i; j < count - 1; j++) {
                    personArray[j] = personArray[j + 1];
                }
                flag = true;
                break;
            }
        }
        if (flag) {
            System.out.println("삭제 후 전체 출력");
            for (int idx = 0 ;  idx < personArray.length ; idx++) {
                Person per = personArray[idx] ;
                if(per == null) {
                    break;
                }
                per.printInfo();
            }
        }
    }

    public void saveToFile() {
        FileOutputStream fis = null;
        ObjectOutputStream oos = null;
        try {
            fis = new FileOutputStream("c:/file/hms.txt");
            oos = new ObjectOutputStream(fis);
            oos.writeObject(personArray);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public Person[] loadToFile() {
        String msg = null;
        FileInputStream fis = null;
        ObjectInputStream oos = null;

        try {
            fis = new FileInputStream("c:/file/hms.txt");
            oos = new ObjectInputStream(fis);
            Person[] perAry = (Person[]) oos.readObject();
            for (int i = 0; i < perAry.length; i++) {
                if (perAry[i] != null) {
                    System.out.println(perAry[i].printInfo());
                    personArray[i] = perAry[i];
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return personArray;
    }
}
