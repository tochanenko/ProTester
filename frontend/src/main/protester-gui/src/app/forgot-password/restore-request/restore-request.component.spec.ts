import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RestoreRequestComponent } from './restore-request.component';

describe('RestoreRequestComponent', () => {
  let component: RestoreRequestComponent;
  let fixture: ComponentFixture<RestoreRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RestoreRequestComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RestoreRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
