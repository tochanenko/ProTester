import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogUtilComponent } from './dialog-util.component';

describe('DialogUtilComponent', () => {
  let component: DialogUtilComponent;
  let fixture: ComponentFixture<DialogUtilComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DialogUtilComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogUtilComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
