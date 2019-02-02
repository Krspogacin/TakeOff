import { Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-hotel-dialog',
  templateUrl: './hotel-dialog.component.html',
  styleUrls: ['./hotel-dialog.component.css']
})
export class HotelDialogComponent implements OnInit {

  hotelUpdateForm: FormGroup;


  constructor(private dialogRef: MatDialogRef<HotelDialogComponent>,
              private formBuilder: FormBuilder,
             @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
    this.hotelUpdateForm = this.formBuilder.group({
      name: [this.data.name,Validators.required],
      address: [this.data.address,Validators.required],
      description: [this.data.description]
    });
  }

  submitForm() {
    const updatedHotel = this.hotelUpdateForm.value;
    updatedHotel.id = this.data.id;
    this.dialogRef.close(updatedHotel);
  }

}
