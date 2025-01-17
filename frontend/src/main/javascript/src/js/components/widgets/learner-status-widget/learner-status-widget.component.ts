/*
 * Copyright 2015 - 2019 TU Dortmund
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {IIntervalService, IPromise} from 'angular';
import {LearnerResource} from '../../../services/resources/learner-resource.service';
import {ToastService} from '../../../services/toast.service';
import {Project} from '../../../entities/project';

/**
 * The directive of the dashboard widget that displays the current status of the learner.
 */
class LearnerStatusWidgetComponent {

  /** The status of the learner. */
  public status: any = null;

  /** The current project. */
  public project: Project = null;

  /** The interval handle. */
  public intervalHandle: IPromise<any> = null;

  /**
   * Constructor.
   *
   * @param $interval
   * @param learnerResource
   * @param toastService
   */
  /* @ngInject */
  constructor(private $interval: IIntervalService,
              private learnerResource: LearnerResource,
              private toastService: ToastService) {
  }

  $onInit(): void {
    this.getStatus();
    this.intervalHandle = this.$interval(() => this.getStatus(), 5000);
  }

  $onDestroy(): void {
    this.$interval.cancel(this.intervalHandle);
  }

  getStatus(): void {
    this.learnerResource.getStatus(this.project.id)
      .then(status => this.status = status)
      .catch(console.error);
  }

  /**
   * Induces the Learner to stop learning after the current hypothesis model.
   */
  abort(): void {
    this.learnerResource.stop(this.project.id)
      .then(() => {
        this.toastService.info('The learner stops as soon as possible.');
      })
      .catch(console.error);
  }
}

export const learnerStatusWidgetComponent = {
  template: require('./learner-status-widget.component.html'),
  bindings: {
    project: '='
  },
  controller: LearnerStatusWidgetComponent,
  controllerAs: 'vm'
};
