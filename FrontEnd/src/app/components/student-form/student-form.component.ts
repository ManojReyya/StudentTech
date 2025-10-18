import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Student } from '../../models/student.model';

@Component({
  selector: 'app-student-form',
  templateUrl: './student-form.component.html',
  styleUrls: ['./student-form.component.css']
})
export class StudentFormComponent implements OnInit {
  @Input() student: Student | null = null;
  @Output() studentSaved = new EventEmitter<Student>();
  @Output() cancelled = new EventEmitter<void>();

  studentName: string = '';
  availableTechStacks: string[] = ['Java', 'Python', 'MSB', 'Angular', 'React', 'Node.js', 'Spring Boot'];
  selectedTechStacks: { [key: string]: boolean } = {};
  customTechStack: string = '';

  ngOnInit(): void {
    // Initialize tech stacks checkboxes
    this.availableTechStacks.forEach(tech => {
      this.selectedTechStacks[tech] = false;
    });

    // If editing, populate the form
    if (this.student) {
      this.studentName = this.student.name;
      this.student.techStacks.forEach(tech => {
        if (this.availableTechStacks.includes(tech)) {
          this.selectedTechStacks[tech] = true;
        } else {
          // Add custom tech stack if not in predefined list
          this.availableTechStacks.push(tech);
          this.selectedTechStacks[tech] = true;
        }
      });
    }
  }

  onTechStackChange(techStack: string, isChecked: boolean): void {
    this.selectedTechStacks[techStack] = isChecked;
  }

  addCustomTechStack(): void {
    const trimmed = this.customTechStack.trim();
    if (trimmed && !this.availableTechStacks.includes(trimmed)) {
      this.availableTechStacks.push(trimmed);
      this.selectedTechStacks[trimmed] = true;
      this.customTechStack = '';
    }
  }

  onSubmit(): void {
    if (!this.studentName.trim()) {
      alert('Please enter student name');
      return;
    }

    const selectedTechs = Object.keys(this.selectedTechStacks)
      .filter(key => this.selectedTechStacks[key]);

    if (selectedTechs.length === 0) {
      alert('Please select at least one tech stack');
      return;
    }

    const studentData: Student = {
      id: this.student?.id,
      name: this.studentName.trim(),
      techStacks: selectedTechs
    };

    this.studentSaved.emit(studentData);
    this.resetForm();
  }

  onCancel(): void {
    this.cancelled.emit();
    this.resetForm();
  }

  resetForm(): void {
    this.studentName = '';
    Object.keys(this.selectedTechStacks).forEach(key => {
      this.selectedTechStacks[key] = false;
    });
    this.customTechStack = '';
  }

  getSelectedCount(): number {
    return Object.values(this.selectedTechStacks).filter(v => v).length;
  }
}