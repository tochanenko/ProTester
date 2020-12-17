import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ViewRoutingModule } from './view-routing.module';
import {TestCasesModule} from "./test-cases/test-cases.module";


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    ViewRoutingModule,
    TestCasesModule
  ]
})
export class ViewModule { }
