import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SysAdminDialogComponent } from './sys-admin-dialog.component';

describe('SysAdminDialogComponent', () => {
  let component: SysAdminDialogComponent;
  let fixture: ComponentFixture<SysAdminDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SysAdminDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SysAdminDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
