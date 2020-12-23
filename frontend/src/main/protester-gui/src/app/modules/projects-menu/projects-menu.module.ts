import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ProjectsMenuRoutingModule} from './projects-menu-routing.module';
import {ViewComponent} from './view/view.component';
import {MaterialModule} from "../../services/material.module";
import {DatasetsModule} from "./datasets/datasets.module";
import {ProjectsModule} from "./projects/projects.module";
import {ResultsModule} from './results/results.module';


@NgModule({
  declarations: [ViewComponent],
  imports: [
    CommonModule,
    ProjectsMenuRoutingModule,
    MaterialModule,
    DatasetsModule,
    ProjectsModule,
    ResultsModule
  ]
})
export class ProjectsMenuModule { }
