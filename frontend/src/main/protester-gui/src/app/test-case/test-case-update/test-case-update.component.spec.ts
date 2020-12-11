import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TestCaseUpdateComponent } from './test-case-update.component';

describe('TestCaseUpdateComponent', () => {
  let component: TestCaseUpdateComponent;
  let fixture: ComponentFixture<TestCaseUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TestCaseUpdateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TestCaseUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
