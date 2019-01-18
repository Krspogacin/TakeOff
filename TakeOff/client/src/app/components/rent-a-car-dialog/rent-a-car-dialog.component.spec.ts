import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RentACarDialogComponent } from './rent-a-car-dialog.component';

describe('RentACarDialogComponent', () => {
  let component: RentACarDialogComponent;
  let fixture: ComponentFixture<RentACarDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RentACarDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RentACarDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
