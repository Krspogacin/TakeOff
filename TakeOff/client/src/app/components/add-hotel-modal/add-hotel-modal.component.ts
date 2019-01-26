import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

export interface DialogData {
  hotel: {};
}

@Component({
  selector: 'app-add-hotel-modal',
  templateUrl: 'add-hotel-modal.component.html',
})
export class AddHotelModalComponent {

  constructor(
    public dialogRef: MatDialogRef<AddHotelModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) { }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
