import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompoundViewComponent } from './compound-view.component';

describe('CompoundViewComponent', () => {
  let component: CompoundViewComponent;
  let fixture: ComponentFixture<CompoundViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompoundViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CompoundViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
