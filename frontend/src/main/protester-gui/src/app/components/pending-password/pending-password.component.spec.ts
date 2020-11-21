import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PendingPasswordComponent } from './pending-password.component';

describe('PendingPasswordComponent', () => {
  let component: PendingPasswordComponent;
  let fixture: ComponentFixture<PendingPasswordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PendingPasswordComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PendingPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
