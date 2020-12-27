import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TestCaseResultTableComponent } from './test-case-result-table.component';

describe('TestCaseResultTableComponent', () => {
  let component: TestCaseResultTableComponent;
  let fixture: ComponentFixture<TestCaseResultTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TestCaseResultTableComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TestCaseResultTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
