import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {TestCaseAnalyzeComponent} from './test-case-analyze.component';
import {RouterModule} from '@angular/router';
import { TestCaseInfoComponent } from './test-case-info/test-case-info.component';
import { ResultListComponent } from './result-list/result-list.component';
import {FlexLayoutModule } from '@angular/flex-layout';
import {MaterialModule} from '../services/material.module';

@NgModule({
  declarations: [TestCaseAnalyzeComponent, TestCaseInfoComponent, ResultListComponent],
  imports: [
    CommonModule,
    FlexLayoutModule,
    MaterialModule,
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
