import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VehicleReservationDialogComponent } from './vehicle-reservation-dialog.component';

describe('VehicleReservationDialogComponent', () => {
  let component: VehicleReservationDialogComponent;
  let fixture: ComponentFixture<VehicleReservationDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VehicleReservationDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VehicleReservationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
