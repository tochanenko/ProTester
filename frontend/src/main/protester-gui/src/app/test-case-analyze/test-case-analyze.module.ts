import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TestCaseAnalyzeComponent} from './test-case-analyze.component';
import {RouterModule} from '@angular/router';
import {TestCaseInfoComponent} from './test-case-info/test-case-info.component';
import {ResultListComponent} from './result-list/result-list.component';
import {FlexLayoutModule} from '@angular/flex-layout';
import {MaterialModule} from '../services/material.module';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatChipsModule} from '@angular/material/chips';
import {FormsModule} from '@angular/forms';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatSortModule} from '@angular/material/sort';

@NgModule({
  declarations: [TestCaseAnalyzeComponent, TestCaseInfoComponent, ResultListComponent],
  imports: [
    CommonModule,
    FlexLayoutModule,
    MaterialModule,
    RouterModule.forChild([
      {
        path: 'test-case-analyze/:id', component: TestCaseAnalyzeComponent
        // canActivate: [AuthGuard],
        // data: {
        //   roles: ['ADMIN', 'MANAGER', 'ENGINEER']
        // }
      }
    ]),
    MatExpansionModule,
    MatChipsModule,
    FormsModule,
    MatProgressSpinnerModule,
    MatSortModule
  ],
  exports: [RouterModule]
})
export class TestCaseAnalyzeModule { }
