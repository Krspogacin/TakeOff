import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OfficeDialogComponent } from './office-dialog.component';

describe('OfficeDialogComponent', () => {
  let component: OfficeDialogComponent;
  let fixture: ComponentFixture<OfficeDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OfficeDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OfficeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
