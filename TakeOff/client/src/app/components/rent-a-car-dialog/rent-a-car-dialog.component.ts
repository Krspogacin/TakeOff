import { Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { RentACarService } from 'src/app/services/rent-a-car/rent-a-car.service';

@Component({
  selector: 'app-rent-a-car-dialog',
  templateUrl: './rent-a-car-dialog.component.html',
  styleUrls: ['./rent-a-car-dialog.component.css']
})
export class RentACarDialogComponent implements OnInit {

  rentACarForm: FormGroup;
  update = true;

  constructor(private rentACarService: RentACarService,
              private dialogRef: MatDialogRef<RentACarDialogComponent>,
              private formBuilder: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public rentACar: any) { }

    ngOnInit(): void {
      if (!this.rentACar) {
        this.rentACar = {'name': '', 'address': '', 'description': ''};
        this.update = false;
      }
      this.rentACarForm = this.formBuilder.group({
         name: [this.rentACar.name, Validators.required],
         address: [this.rentACar.address, Validators.required],
         description: [this.rentACar.description]
      });
    }

    submitForm() {
      const nameControl: AbstractControl = this.rentACarForm.get('name');
      this.rentACarService.checkName(nameControl.value).subscribe(
        () => {
          const updatedRentACar = this.rentACarForm.value;
          updatedRentACar.id = this.rentACar.id;
          this.dialogRef.close(updatedRentACar);
        },
        () => {
          nameControl.setErrors({ nameExists: true });
          const element = document.getElementById('scrollId');
          element.scrollTo(0, 0);
        }
      );
    }
}
