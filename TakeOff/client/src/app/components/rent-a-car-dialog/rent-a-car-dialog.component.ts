import { Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-rent-a-car-dialog',
  templateUrl: './rent-a-car-dialog.component.html',
  styleUrls: ['./rent-a-car-dialog.component.css']
})
export class RentACarDialogComponent implements OnInit {

  rentACarUpdateForm: FormGroup;

  constructor(private dialogRef: MatDialogRef<RentACarDialogComponent>,
              private formBuilder: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public data: any) { }

    ngOnInit(): void {
      this.rentACarUpdateForm = this.formBuilder.group({
         name: [this.data.name],
         address: [this.data.address],
         description: [this.data.description]
      });
    }

    submitForm() {
      const updatedRentACar = this.rentACarUpdateForm.value;
      updatedRentACar.id = this.data.id;
      this.dialogRef.close(updatedRentACar);
    }
}
