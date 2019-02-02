import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RentACarMainServiceDialogComponent } from './rent-a-car-main-service-dialog.component';

describe('RentACarMainServiceDialogComponent', () => {
  let component: RentACarMainServiceDialogComponent;
  let fixture: ComponentFixture<RentACarMainServiceDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RentACarMainServiceDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RentACarMainServiceDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
