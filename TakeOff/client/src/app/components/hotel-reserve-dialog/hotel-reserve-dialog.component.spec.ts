import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HotelReserveDialogComponent } from './hotel-reserve-dialog.component';

describe('HotelReserveDialogComponent', () => {
  let component: HotelReserveDialogComponent;
  let fixture: ComponentFixture<HotelReserveDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HotelReserveDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HotelReserveDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
