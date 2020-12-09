import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {TestCaseAnalyzeComponent} from './test-case-analyze.component';
import {MatCardModule} from '@angular/material/card';
import {RouterModule} from '@angular/router';

@NgModule({
  declarations: [TestCaseAnalyzeComponent],
  imports: [
    CommonModule,
    MatCardModule,
    RouterModule.forChild([
      {
        path: 'test-case-analyze', component: TestCaseAnalyzeComponent
        // canActivate: [AuthGuard],
        // data: {
        //   roles: ['ADMIN', 'MANAGER', 'ENGINEER']
        // }
      }
    ])
  ],
  exports: [RouterModule]
})
export class TestCaseAnalyzeModule { }
