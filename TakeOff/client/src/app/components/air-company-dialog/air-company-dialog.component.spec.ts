import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AirCompanyDialogComponent } from './air-company-dialog.component';

describe('AirCompanyDialogComponent', () => {
  let component: AirCompanyDialogComponent;
  let fixture: ComponentFixture<AirCompanyDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AirCompanyDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AirCompanyDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
