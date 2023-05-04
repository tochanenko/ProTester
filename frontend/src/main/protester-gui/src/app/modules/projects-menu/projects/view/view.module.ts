import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ViewRoutingModule } from './view-routing.module';
import {TestCasesModule} from "./test-cases/test-cases.module";
import {EnvironmentModule} from "./environment/environment.module";


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    ViewRoutingModule,
    TestCasesModule,
    EnvironmentModule
  ]
})
export class ViewModule { }
