import { Component, OnInit } from '@angular/core';
import { StudentService } from '../../services/student.service';
import { Student } from '../../models/student.model';

@Component({
  selector: 'app-student-list',
  templateUrl: './student-list.component.html',
  styleUrls: ['./student-list.component.css']
})
export class StudentListComponent implements OnInit {
  students: Student[] = [];
  filteredStudents: Student[] = [];
  availableTechStacks: string[] = [];
  selectedTechStacks: { [key: string]: boolean } = {};
  editingStudent: Student | null = null;
  showAddForm: boolean = false;

  constructor(private studentService: StudentService) { }

  ngOnInit(): void {
    this.loadStudents();
    this.loadTechStacks();
  }

  loadStudents(): void {
    this.studentService.getAllStudents().subscribe(students => {
      this.students = students;
      this.applyFilter();
    });
  }

  loadTechStacks(): void {
    this.studentService.getAllTechStacks().subscribe(techStacks => {
      this.availableTechStacks = techStacks;
      this.availableTechStacks.forEach(tech => {
        this.selectedTechStacks[tech] = false;
      });
    });
  }

  onTechStackChange(techStack: string, isChecked: boolean): void {
    this.selectedTechStacks[techStack] = isChecked;
    this.applyFilter();
  }

  applyFilter(): void {
    const selected = Object.keys(this.selectedTechStacks)
      .filter(key => this.selectedTechStacks[key]);
    
    this.studentService.filterStudentsByTechStacks(selected).subscribe(filtered => {
      this.filteredStudents = filtered;
    });
  }

  clearFilters(): void {
    Object.keys(this.selectedTechStacks).forEach(key => {
      this.selectedTechStacks[key] = false;
    });
    this.applyFilter();
  }

  getTechStackBadgeClass(techStack: string): string {
    const lowerTech = techStack.toLowerCase();
    if (lowerTech === 'java') return 'badge-java';
    if (lowerTech === 'python') return 'badge-python';
    if (lowerTech === 'msb') return 'badge-msb';
    if (lowerTech === 'angular') return 'badge-angular';
    return 'badge-default';
  }

  editStudent(student: Student): void {
    this.editingStudent = { ...student };
    this.showAddForm = false;
  }

  deleteStudent(id: number | undefined): void {
    if (id && confirm('Are you sure you want to delete this student?')) {
      this.studentService.deleteStudent(id).subscribe(
        () => {
          this.loadStudents();
          this.loadTechStacks();
        },
        (error) => {
          alert('Error deleting student: ' + (error.error?.message || error.message));
        }
      );
    }
  }

  cancelEdit(): void {
    this.editingStudent = null;
  }

  saveStudent(student: Student): void {
    if (student.id) {
      this.studentService.updateStudent(student.id, student).subscribe(
        () => {
          this.editingStudent = null;
          this.loadStudents();
          this.loadTechStacks();
        },
        (error) => {
          alert('Error updating student: ' + (error.error?.message || error.message));
        }
      );
    }
  }

  toggleAddForm(): void {
    this.showAddForm = !this.showAddForm;
    this.editingStudent = null;
  }

  addStudent(student: Student): void {
    this.studentService.addStudent(student).subscribe(
      () => {
        this.showAddForm = false;
        this.loadStudents();
        this.loadTechStacks();
      },
      (error) => {
        alert('Error adding student: ' + (error.error?.message || error.message));
      }
    );
  }

  getSelectedTechStacksCount(): number {
    return Object.values(this.selectedTechStacks).filter(v => v).length;
  }
}