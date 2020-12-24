import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TestCaseResultTableComponent } from './test-case-result-table/test-case-result-table.component';
import {MaterialModule} from "../../services/material.module";
import {TestCaseRoutingModule} from "./test-case-routing.module";



@NgModule({
  declarations: [TestCaseResultTableComponent],
  imports: [
    CommonModule,
    TestCaseRoutingModule,
    MaterialModule
  ]
})
export class TestCaseModule { }
