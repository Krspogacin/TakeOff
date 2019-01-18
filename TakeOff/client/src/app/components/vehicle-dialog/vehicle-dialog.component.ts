import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-vehicle-dialog',
  templateUrl: './vehicle-dialog.component.html',
  styleUrls: ['./vehicle-dialog.component.css']
})
export class VehicleDialogComponent implements OnInit {

  vehicleForm: FormGroup;
  currentYear: number;
  minYear = 1800;
  selectedFile: File;
  imgSrc: string;
  update = false;

  constructor(private dialogRef: MatDialogRef<VehicleDialogComponent>,
             private formBuilder: FormBuilder,
             @Inject(MAT_DIALOG_DATA) public vehicle: any) { }

  ngOnInit(): void {
    this.currentYear = new Date().getFullYear();
    if (!this.vehicle) {
      this.vehicle = {'brand': '', 'model': '', 'year': '', 'reserved': false};
    } else {
      this.update = true;
      this.imgSrc = this.vehicle.image;
    }
    this.vehicleForm = this.formBuilder.group({
      brand: [this.vehicle.brand, Validators.required],
      model: [this.vehicle.model, Validators.required],
      year: [this.vehicle.year, [Validators.required, Validators.min(1800), Validators.max(this.currentYear)]]
    });
  }

  submitForm() {
    if (this.vehicleForm.valid) {
      const newVehicle = this.vehicleForm.value;
      newVehicle.id = this.vehicle.id;
      newVehicle.reserved = this.vehicle.reserved;
      newVehicle.image = this.imgSrc;
      this.dialogRef.close(newVehicle);
    }
  }

  onFileSelected(event) {
    if (event.target.files && event.target.files[0]) {
      this.selectedFile = <File>event.target.files[0];
      const reader = new FileReader();
      reader.onload = () => {
        this.imgSrc = reader.result.toString();
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }
}
