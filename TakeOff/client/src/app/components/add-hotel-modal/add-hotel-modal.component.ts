import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef} from '@angular/material';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-add-hotel-modal',
  templateUrl: 'add-hotel-modal.component.html',
})
export class AddHotelModalComponent implements OnInit{
  
  hotelForm: FormGroup;

  constructor(private dialogRef: MatDialogRef<AddHotelModalComponent>) { }

    ngOnInit() {
      this.hotelForm = new FormGroup({
        name: new FormControl(),
        address: new FormControl(),
        description: new FormControl(),
     });
    }

}
