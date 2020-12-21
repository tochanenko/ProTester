import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ResultsRoutingModule} from './results-routing.module';
import {MaterialModule} from '../../../services/material.module';
import {AnalyzeComponent} from './analyze/analyze.component';
import {TestCaseInfoComponent} from './analyze/test-case-info/test-case-info.component';
import {ResultListComponent} from './analyze/result-list/result-list.component';
import {FlexLayoutModule} from '@angular/flex-layout';
import {FormsModule} from '@angular/forms';

@NgModule({
  declarations: [TestCaseInfoComponent, ResultListComponent, AnalyzeComponent],
  imports: [
    CommonModule,
    ResultsRoutingModule,
    MaterialModule,
    FlexLayoutModule,
    MaterialModule,
    FormsModule
  ]
})
export class ResultsModule {
}
